package akki.service;

import java.util.Set;

import javax.annotation.PostConstruct;

import org.lognet.springboot.grpc.GRpcService;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Sets;
import com.google.protobuf.GeneratedMessageV3;

import akki.exception.GrpcException;
import akki.service.behavior.GrpcBehavior;
import io.grpc.stub.StreamObserver;
import spec.AkkiServiceGrpc.AkkiServiceImplBase;
import spec.request.ChatMessageRequest;
import spec.response.ChatMessageResponse;

@GRpcService
public class GrpcService extends AkkiServiceImplBase {

    @Autowired
    private SaveMessageService saveMessageService;

    @Autowired
    private GetMessageService getMessageService;

    @PostConstruct
    public void postConstruct() {
    }

    /**
     * Server Streaming RPC用<br>
     * client側をメモリ保持
     */
    private Set<StreamObserver<ChatMessageResponse>> chatClients = Sets.newConcurrentHashSet();

    /**
     * Unary RPC<br>
     * 実行共通メソッド
     * 
     * @param <RQ>
     * @param <RS>
     * @param service
     * @param request
     * @param responseObserver
     */
    private <RQ extends GeneratedMessageV3, RS extends GeneratedMessageV3> void execute(
            GrpcBehavior<RQ, RS> service, RQ request, StreamObserver<RS> responseObserver) {

        try {
            // サービスの呼び出し
            RS response = service.execute(request);

            // レスポンス
            responseObserver.onNext(response);
            responseObserver.onCompleted();
        } catch (GrpcException e) {
            // エラー正常としてレスポンス
            responseObserver.onNext(service.responseError(e));
            responseObserver.onCompleted();
        } catch (Throwable t) {
            // エラー正常としてレスポンス
            responseObserver.onNext(service.responseError(new GrpcException(t)));
            responseObserver.onCompleted();
        }
    }

    /**
     * Server Streaming RPC<br>
     * 実行共通メソッド
     * 
     * @param <RQ>
     * @param <RS>
     * @param service
     * @param request
     * @param responseObserver
     */
    private <RQ extends GeneratedMessageV3, RS extends GeneratedMessageV3> void executeStream(
            GrpcBehavior<RQ, RS> service, RQ request, StreamObserver<RS> responseObserver) {

        try {
            // サービスの呼び出し
            RS response = service.execute(request);

            // レスポンス
            responseObserver.onNext(response);

            // responseObserver.onCompletedタイミングの検討
            // Client Server Streaming RPCがconnect(grpc-web)で対応していないため接続したままにするしかないかもしれない
            // https://connectrpc.com/docs/faq/#is-streaming-supported
        } catch (GrpcException e) {
            // エラー正常としてレスポンス
            responseObserver.onNext(service.responseError(e));
            responseObserver.onCompleted();
        } catch (Throwable t) {
            // エラー正常としてレスポンス
            responseObserver.onNext(service.responseError(new GrpcException(t)));
            responseObserver.onCompleted();
        }
    }

    @Override
    public void saveChatMessage(ChatMessageRequest request,
            StreamObserver<ChatMessageResponse> responseObserver) {

        execute(saveMessageService, request, responseObserver);
        // メッセージ取得API
        ChatMessageResponse messageResponse;
        try {
            // server streaming
            messageResponse = getMessageService.execute(ChatMessageRequest.newBuilder()
                    .setChat(request.getChat())
                    .build());
            boardCast(messageResponse);
        } catch (Throwable e) {
            closeConnection();
        }
    }

    @Override
    public void getChatMessage(ChatMessageRequest request,
            StreamObserver<ChatMessageResponse> responseObserver) {
        // connectionに対するresponseObserverをメモリ保持しておく
        chatClients.add(responseObserver);
        // メッセージ取得API
        executeStream(getMessageService, request, responseObserver);
    }

    /**
     * メッセージ更新の度にメッセージをconnection確立先に返答する
     * 
     * @param msg
     */
    private void boardCast(ChatMessageResponse msg) {
        for (StreamObserver<ChatMessageResponse> resp : chatClients) {
            try {
                resp.onNext(msg);
            } catch (Exception e) {
                resp.onCompleted();
            }
        }
    }

    /**
     * connection確立先を閉じる
     */
    private void closeConnection() {
        chatClients.forEach(StreamObserver<ChatMessageResponse>::onCompleted);
    }
}
