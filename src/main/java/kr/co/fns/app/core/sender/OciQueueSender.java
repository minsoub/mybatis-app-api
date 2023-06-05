package kr.co.fns.app.core.sender;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.bmc.queue.QueueAsyncClient;
import com.oracle.bmc.queue.model.PutMessagesDetails;
import com.oracle.bmc.queue.model.PutMessagesDetailsEntry;
import com.oracle.bmc.queue.requests.PutMessagesRequest;
import com.oracle.bmc.queue.responses.PutMessagesResponse;
import com.oracle.bmc.responses.AsyncHandler;
import kr.co.fns.app.core.receiver.FanitData;
import kr.co.fns.app.core.receiver.OciProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

//@Component
@AllArgsConstructor
@Slf4j
@Component
public class OciQueueSender {

    private final ObjectMapper objectMapper;
    private final QueueAsyncClient queueAsyncClient;
    private final OciProperties ociProperties;

    /**
     * 메시지 전송 함수
     * @throws Exception
     */
    public void sendMessage(FanitData fanitData) throws Exception {
//        FanitData fanitData = FanitData.builder()
//                .integUid("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx")
//                .rewardType(RewardType.CLUB_JOIN_REWARD)
//                .type("1")
//                .userIp("10.0.0.1")
//                .countryCode("kr")
//                .country("KOREA")
//                .fanidId(null)
//                .fainidDetailId(null)
//                .build();

        String message = objectMapper.writeValueAsString(fanitData);
        PutMessagesDetails putMessagesDetails = PutMessagesDetails.builder()
                .messages(new ArrayList<>(Arrays.asList(PutMessagesDetailsEntry.builder().content(message).build())))
                .build();

        PutMessagesRequest putMessagesRequest = PutMessagesRequest.builder()
                .queueId(ociProperties.getQueueName())
                .putMessagesDetails(putMessagesDetails)
                .build();

        ResponseHandler<PutMessagesRequest, PutMessagesResponse> sendHandler = new ResponseHandler<>();
        queueAsyncClient.putMessages(putMessagesRequest, sendHandler);
        PutMessagesResponse putMessageResponse = sendHandler.waitForCompletion();
        log.debug(putMessageResponse.toString());
        return;
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
