package kr.co.fns.app.core.receiver;

import org.springframework.context.ApplicationEvent;

public class OciQueueEvent extends ApplicationEvent {

    private final FanitData object;

    public OciQueueEvent(Object source, FanitData customObject) {
        super(source);
        this.object = customObject;
    }

    public FanitData getData() {
        return object;
    }
}
