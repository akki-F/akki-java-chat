package akki.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import akki.exception.GrpcException;
import akki.model.entity.MessageEntity;
import akki.repository.MessageRepository;
import akki.service.base.BaseService;
import spec.model.ChatMessageModel;
import spec.request.ChatMessageRequest;
import spec.response.ChatMessageResponse;

/**
 * メッセージ更新API
 */
@Service
public class SaveMessageService extends BaseService<ChatMessageRequest, ChatMessageResponse> {


    @Autowired
    private MessageRepository messageRepository;

    @Override
    public ChatMessageResponse executeProcess(ChatMessageRequest request) throws Exception {
        var builder = ChatMessageResponse.newBuilder();

        // requestモデル作成
        var formRequest = createMessageEntity(request);

        // メッセージを登録・更新する
        messageRepository.upsertMessage(formRequest);

        return builder.build();
    }

    @Override
    public ChatMessageResponse createErrorResponse(GrpcException e) {
        return ChatMessageResponse.newBuilder()
                .addChatMessage(ChatMessageModel.newBuilder().setMessageContent("missing"))
                .build();
    }

    /**
     * メッセージリクエストを作成する
     * 
     * @param request
     * @return Estimate
     * @throws GrpcException
     */
    protected MessageEntity createMessageEntity(ChatMessageRequest request)
            throws GrpcException {
        var chat = request.getChat();

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setMessageSeq(chat.getMessageSeq());
        messageEntity.setMessageContent(chat.getMessageContent());
        messageEntity.setPicName(chat.getPicName());

        // user情報
        var user = request.getUser();
        messageEntity.setCreatedUser(user.getCreatedUser());
        messageEntity.setCreatedFunc(user.getCreatedFunc());
        messageEntity.setUpdatedUser(user.getUpdatedUser());
        messageEntity.setUpdatedFunc(user.getUpdatedFunc());

        return messageEntity;
    }
}
