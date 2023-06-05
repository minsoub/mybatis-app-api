package kr.co.fns.app.core.receiver;

import com.google.gson.Gson;
import com.oracle.bmc.queue.QueueAsyncClient;
import com.oracle.bmc.queue.model.GetMessage;
import com.oracle.bmc.queue.model.GetMessages;
import com.oracle.bmc.queue.requests.DeleteMessageRequest;
import com.oracle.bmc.queue.requests.GetMessagesRequest;
import com.oracle.bmc.queue.responses.DeleteMessageResponse;
import com.oracle.bmc.queue.responses.GetMessagesResponse;
import com.oracle.bmc.responses.AsyncHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.naming.NoInitialContextException;
import java.util.concurrent.CountDownLatch;

@Component
@AllArgsConstructor
@Slf4j
public class OCIQueueMessageReceiver {

    private final ApplicationEventPublisher eventPublisher;
    private final QueueAsyncClient queueAsyncClient;
    private final OciProperties ociProperties;

    @Scheduled(fixedDelay = 5000)
    public void receiveMessage() throws Exception {
        log.debug(">> recievedMessage schedule start=========== {}", ociProperties.getQueueName());
        try {
            // 대기열 메시지 수신 요청 생성
            GetMessagesRequest receiveMessagesRequest = GetMessagesRequest.builder()
                    .queueId(ociProperties.getQueueName())
                    .build();

            ResponseHandler<GetMessagesRequest, GetMessagesResponse> responseHandler = new ResponseHandler<>();

            queueAsyncClient.getMessages(receiveMessagesRequest, responseHandler);

            GetMessagesResponse responseMessage = responseHandler.waitForCompletion();
            GetMessages messages = responseMessage.getGetMessages();

            for (GetMessage message : messages.getMessages()) {
                // 메시지 처리 로직 작성
                log.debug("rcv message => {}", message);
                String messageBody = message.getContent();
                try {
                    FanitData obj = new Gson().fromJson(messageBody, FanitData.class);

                    OciQueueEvent event = new OciQueueEvent(this, obj);
                    eventPublisher.publishEvent(event);

                    log.debug("message request id => {}", message.getReceipt());
                    // 메시지 삭제 요청
                    DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                            .queueId(ociProperties.getQueueName())
                            .messageReceipt(message.getReceipt())
                            //.opcRequestId(message.)
                            .build();

                    // 메시지 삭제
                    ResponseHandler<DeleteMessageRequest, DeleteMessageResponse> deleteHandler = new ResponseHandler<>();
                    queueAsyncClient.deleteMessage(deleteMessageRequest, deleteHandler);
                    DeleteMessageResponse deleteMessageResponse = deleteHandler.waitForCompletion();
                    log.debug(deleteMessageResponse.toString());
                    log.debug("delete => {}", deleteMessageResponse.get__httpStatusCode__());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        } catch (NoInitialContextException ex) {
            // 초기 로딩시 시간이 필요하다.
        }
        log.debug(">> recievedMessage schedule end===========");
    }

    /**
     * 공통 핸들러
     * @param <IN>
     * @param <OUT>
     */
    private static class ResponseHandler<IN, OUT> implements AsyncHandler<IN, OUT> {
        private OUT item;
        private Throwable failed = null;
        private CountDownLatch latch = new CountDownLatch(1);

        private OUT waitForCompletion() throws Exception {
            latch.await();
            if (failed != null) {
                if (failed instanceof Exception) {
                    throw (Exception) failed;
                }
                throw (Error) failed;
            }
            return item;
        }

        @Override
        public void onSuccess(IN request, OUT response) {
            item = response;
            latch.countDown();
        }

        @Override
        public void onError(IN request, Throwable error) {
            failed = error;
            latch.countDown();
        }
    }
}
