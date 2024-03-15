package akki.config;

import java.util.concurrent.TimeUnit;

import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import io.grpc.ServerBuilder;
import io.grpc.netty.shaded.io.grpc.netty.NettyServerBuilder;
import io.grpc.netty.shaded.io.netty.handler.ssl.util.InsecureTrustManagerFactory;

/**
 * gRPC の設定を行う<br>
 * Server Streaming RPC と Unary RPC が混在する状況において、gRPC のタイムアウト設計のベストプラクティスが構築できていない。
 */
@Configuration(proxyBeanMethods = false)
public class GrpcConfig extends GRpcServerBuilderConfigurer {

    /** gRPC の処理を行うスレッド名 */
    public static String GRPC_SERVER_THREAD_NAME;

    /** gRPC の処理を行うスレッド数 */
    public static int GRPC_SERVER_THREAD_COUNT;

    /** gRPC の処理を行うスレッド名 */
    public static String GRPC_CLIENT_THREAD_NAME;

    /** gRPC の処理を行うスレッド数 */
    public static int GRPC_CLIENT_THREAD_COUNT;

    /** channel のデフォルト maxInboundMessageSize(定数) */
    public static DataSize GRPC_DEFAULT_MAX_INBOUND_MESSAGE_SIZE = DataSize.ofMegabytes(30);

    /** channel のデフォルト maxInboundMessageSize */
    public static DataSize GRPC_CHANNEL_MAX_INBOUND_MESSAGE_SIZE;

    /** channel のデフォルト maxInboundMetadataSize(定数) */
    public static DataSize GRPC_DEFAULT_MAX_INBOUND_METADATA_SIZE = DataSize.ofKilobytes(16);

    /** channel のデフォルト maxInboundMetadataSize */
    public static DataSize GRPC_CHANNEL_MAX_INBOUND_METADATA_SIZE;

    /**
     * gRPC の Keepalive に設定する時間
     * {@link NettyServerBuilder} にコードがある。
     *
     * @see <a href="https://github.com/grpc/grpc/blob/master/doc/keepalive.md">Keepalive User Guide for gRPC Core (and dependents)</a>
     */
    public static long GRPC_KEEPALIVE_TIME_MS;

    /**
     * gRPC の Keepalive timeout に設定する時間
     * {@link NettyServerBuilder} にコードがある。
     *
     * @see <a href="https://github.com/grpc/grpc/blob/master/doc/keepalive.md">Keepalive User Guide for gRPC Core (and dependents)</a>
     */
    public static long GRPC_KEEPALIVE_TIMEOUT_MS;

    /**
     * gRPC client へ Keepalive を許可する時間<br/>
     * {@link #GRPC_KEEPALIVE_TIME_MS} {@literal >} {@link #GRPC_PERMIT_KEEPALIVE_TIME_MS} になるようにすること。
     *
     * @see <a href="https://github.com/grpc/grpc/blob/master/doc/keepalive.md">Keepalive User Guide for gRPC Core (and dependents)</a>
     */
    public static long GRPC_PERMIT_KEEPALIVE_TIME_MS;

    /**
     * gRPC のセキュリティを使用するか<br/>
     * 本設定を元に {@link AuthorizationInterceptor} の動作が決定される。
     */
    public static boolean GRPC_SECURITY_ENABLED;

    /**
     * gRPC のクライアントにて証明書の確認を行うか<br/>
     * 本設定を元に {@link InsecureTrustManagerFactory} の動作が決定される。
     */
    public static boolean GRPC_CHANNEL_CERTIFICATES_VERIFICATION_ENABLED;

    /**
     * gRPC のデバッグ用に BinaryLog を使用するか<br/>
     * 本設定を元に {@link GrpcUtils#useBinaryLog()} の呼び出しがされる。
     */
    public static boolean GRPC_DEBUG_USE_BINARY_LOG;

    /**
     * BeanFactory 生成後に処理。
     *
     * {@literal @Component} より先に処理させるために {@link BeanFactoryPostProcessor} を使用し
     * {@literal @Value} を有効にするためにコンストラクターインジェクションを使用している。
     *
     * @param grpcChannelMaxInboundMessageSize gRPC の maxInboundMessageSize(byte)
     * @param grpcChannelMaxInboundMetadataSize gRPC の maxInboundMetadataSize(byte)
     * @param grpcKeepaliveTimeMs gRPC の Keepalive (ms)
     * @param grpcKeepaliveTimeoutMs gRPC の Keepalive timeout (ms)
     * @param grpcKeepalivePermitTimeMs gRPC client へ Keepalive を許可する時間 (ms)
     * @param grpcSecurityEnabled gRPC のセキュリティを使用するか
     * @return BeanFactory 生成後のカスタマイズ処理を行うファクトリ
     */
    @Bean
    public static BeanFactoryPostProcessor initGrpcConfig(
            @Value("${grpc.server.thread.count:20}") int grpcServerThreadCount,
            @Value("${grpc.client.thread.count:20}") int grpcClientThreadCount,
            @Value("${grpc.channel.maxInboundMessageSize:#{null}}") DataSize grpcChannelMaxInboundMessageSize,
            @Value("${grpc.channel.maxInboundMetadataSize:#{null}}") DataSize grpcChannelMaxInboundMetadataSize,
            @Value("${grpc.keepalive.time:2000}") long grpcKeepaliveTimeMs,
            @Value("${grpc.keepalive.timeout:5000}") long grpcKeepaliveTimeoutMs,
            @Value("${grpc.keepalive.permit_time:1000}") long grpcKeepalivePermitTimeMs
            ) {
        return beanFactory -> {
            GRPC_CHANNEL_MAX_INBOUND_MESSAGE_SIZE = (grpcChannelMaxInboundMessageSize == null) ? GRPC_DEFAULT_MAX_INBOUND_MESSAGE_SIZE : grpcChannelMaxInboundMessageSize;
            GRPC_CHANNEL_MAX_INBOUND_METADATA_SIZE = (grpcChannelMaxInboundMetadataSize == null) ? GRPC_DEFAULT_MAX_INBOUND_METADATA_SIZE : grpcChannelMaxInboundMetadataSize;
            GRPC_KEEPALIVE_TIME_MS = grpcKeepaliveTimeMs;
            GRPC_KEEPALIVE_TIMEOUT_MS = grpcKeepaliveTimeoutMs;
            GRPC_PERMIT_KEEPALIVE_TIME_MS = grpcKeepalivePermitTimeMs;
        };
    }

    /**
     * gRPC のサーバ側を設定する。
     *
     * @see <a href="https://github.com/LogNet/grpc-spring-boot-starter#44-custom-grpc-server-configuration">Custom gRPC Server Configuration</a>
     */
    @Override
    public void configure(final ServerBuilder<?> serverBuilder) {
        if (serverBuilder instanceof NettyServerBuilder) {
            ((NettyServerBuilder) serverBuilder)
                    .maxInboundMessageSize((int) GRPC_CHANNEL_MAX_INBOUND_MESSAGE_SIZE.toBytes())
                    .maxInboundMetadataSize((int) GRPC_CHANNEL_MAX_INBOUND_METADATA_SIZE.toBytes())
                    .keepAliveTime(GRPC_KEEPALIVE_TIME_MS, TimeUnit.MILLISECONDS)
                    .keepAliveTimeout(GRPC_KEEPALIVE_TIMEOUT_MS, TimeUnit.MILLISECONDS)
                    .permitKeepAliveTime(GRPC_PERMIT_KEEPALIVE_TIME_MS, TimeUnit.MILLISECONDS)
                    .permitKeepAliveWithoutCalls(true);
        }
    }
}
