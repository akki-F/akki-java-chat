package akki.service;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import akki.exception.GrpcException;
import akki.model.behavior.MessageSearcher;
import akki.model.entity.MessageEntity;
import akki.repository.MessageRepository;
import akki.service.base.BaseService;
import spec.model.ChatMessageModel;
import spec.request.ChatMessageRequest;
import spec.response.ChatMessageResponse;

/**
 * メッセージ取得API
 */
@Service
public class GetMessageService
        extends BaseService<ChatMessageRequest, ChatMessageResponse> {

    @Autowired
    private MessageRepository messageRepository;

    @Override
    public ChatMessageResponse executeProcess(ChatMessageRequest request) throws Exception {
        var builder = ChatMessageResponse.newBuilder();

        // requestモデル作成
        var formRequest = createRequest(request);

        var messageList = messageRepository.selectMessage(formRequest);

        builder.setUser(request.getUser());
        var messageModelList = messageList.stream()
                .map(this::createMessageModel)
                .collect(Collectors.toList());
        builder.addAllChatMessage(messageModelList);

        return builder.build();
    }

    @Override
    public ChatMessageResponse createErrorResponse(GrpcException e) {
        return ChatMessageResponse.newBuilder()
                .build();
    }

    /**
     * リクエストを作成する
     * 
     * @param request
     * @return MessageSearcher
     * @throws GrpcException
     */
    protected MessageSearcher createRequest(ChatMessageRequest request) throws GrpcException {
        MessageSearcher searcher = new MessageSearcher() {

        };

        return searcher;
    }

    /**
     * ChatMessageModelを作成する
     * @param message
     * @return ChatMessageModel
     */
    protected ChatMessageModel createMessageModel(MessageEntity message) {
        var builder = ChatMessageModel.newBuilder();
        builder.setMessageSeq(message.getMessageSeq());
        builder.setMessageContent(message.getMessageContent());
        builder.setPicName(message.getPicName());

        return builder.build();
    }
}
