package com.zipwhip.api.signals;

import java.io.Serializable;

/**
 * A raw declaration of a signal. Most of the time you'd use a subclass of this to get the important details.
 */
public class Signal implements Serializable {

    private static final long serialVersionUID = 4312231478912373L;

    /**
     * Some signals can be parsed as DTO's. If that's possible it will be in the content.
     */
    Object content;

    String type;
    String scope;
    String uuid;
    String event;
    String reason;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    public String getUri() {
        return "/signal/" + type + "/event";
    }

}
