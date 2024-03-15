package akki.service.base;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.protobuf.GeneratedMessageV3;

import akki.exception.GrpcException;
import akki.service.behavior.GrpcBehavior;

public abstract class BaseService<RQ extends GeneratedMessageV3, RS extends GeneratedMessageV3>
        implements GrpcBehavior<RQ, RS> {

    /**
     * サービスを実行する
     * 
     * @param request
     * @return RS
     */
    public abstract RS executeProcess(RQ request) throws Throwable;

    /**
     * エラー正常のレスポンスを作成する
     * 
     * @param request
     * @param e
     * @return RS (エラー正常レスポンス)
     */
    public abstract RS createErrorResponse(GrpcException e);

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Throwable.class)
    public RS execute(final RQ request) throws Throwable {
        // 実際のサービスの処理を実行する
        RS response = executeProcess(request);
        return response;
    }

    @Override
    public RS responseError(final GrpcException e) {
        return createErrorResponse(e);
    }
}
