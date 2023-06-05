package kr.co.fns.app.core.sender;

import kr.co.fns.app.core.receiver.FanitData;
import org.springframework.context.ApplicationEvent;

/**
 * OCI Queue Sender Event Class
 *
 */
public class OciQueueSenderEvent extends ApplicationEvent {

    private final FanitData object;

    public OciQueueSenderEvent(Object source, FanitData customObject) {
        super(source);
        this.object = customObject;
    }

    public FanitData getData() {
        return object;
    }
}
