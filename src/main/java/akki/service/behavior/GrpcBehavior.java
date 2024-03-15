package akki.service.behavior;

import com.google.protobuf.GeneratedMessageV3;

import akki.exception.GrpcException;

/**
 * gRPC Service IF
 * 実行制御とエラー時制御を強制させる
 * 
 * @param <RQ> gRPC Request
 * @param <RS> gRPC Response
 */
public interface GrpcBehavior<RQ extends GeneratedMessageV3, RS extends GeneratedMessageV3> {
    RS execute(final RQ request) throws Throwable;

    RS responseError(final GrpcException e);
}
