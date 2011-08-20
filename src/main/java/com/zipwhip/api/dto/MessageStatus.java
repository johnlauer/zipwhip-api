package com.zipwhip.api.dto;

/**
 * @author Me
 */
public class MessageStatus {

    private boolean delivered;
    private long id;
    private String uuid;
    private String statusDescription;
    private int statusCode;

    public MessageStatus() {
    }

    public MessageStatus(Message message) {
        this.delivered = (message.getStatusCode() == 0 || message.getStatusCode() == 4);
        this.id = message.getId();
        this.uuid = message.getUuid();
        this.statusDescription = message.getStatusDesc();
        this.statusCode = message.getStatusCode();
    }

    public boolean isDelivered() {
        return delivered;
    }

    public void setDelivered(boolean delivered) {
        this.delivered = delivered;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    @Override
    public String toString() {
        StringBuilder toStringBuilder = new StringBuilder("==> MessageStatus details:");
        toStringBuilder.append("\nDelivered: ").append(delivered);
        toStringBuilder.append("\nId: ").append(Long.toString(id));
        toStringBuilder.append("\nUUID: ").append(uuid);
        toStringBuilder.append("\nStatus Code: ").append(statusCode);
        toStringBuilder.append("\nStatus Description: ").append(statusDescription);
        return toStringBuilder.toString();
    }
}
