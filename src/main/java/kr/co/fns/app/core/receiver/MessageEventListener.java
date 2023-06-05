package kr.co.fns.app.core.receiver;

import kr.co.fns.app.api.fanit.model.request.FanitPointInsertReq;
import kr.co.fns.app.api.fanit.service.FanitOCIService;
import kr.co.fns.app.core.sender.OciQueueSender;
import kr.co.fns.app.core.sender.OciQueueSenderEvent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class MessageEventListener {

    private final OciQueueSender ociQueueSender;

    private final FanitOCIService fanitOCIService;
    @EventListener
    public void handleMessageEvent(OciQueueEvent event) {
        FanitData object = event.getData();
        FanitPointInsertReq fanitPointInsertReq = new FanitPointInsertReq(object.getRewardType(),object);
        fanitOCIService.getFanitPaymentPolicy(fanitPointInsertReq);
        // 수신한 이벤츠 처리 로직.
        log.debug("event Listener => {}", object);
    }

    @EventListener
    public void sendHandleMessageEvent(OciQueueSenderEvent event) throws Exception {
        FanitData object = event.getData();
        ociQueueSender.sendMessage(object);
    }
}
