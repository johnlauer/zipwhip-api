package com.zipwhip.api.dto;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 7/5/11
 * Time: 8:16 PM
 * <p/>
 * Represents a Conversation with someone on Zipwhip.
 */
public class Conversation {

    long id;
    long deviceId;
    String deviceAddress;
    String fingerprint;
    String address;
    String cc;
    String bcc;
    int unreadCount;
    long lastContactId;
    boolean isNew;
    boolean deleted;
    int version;
    long lastContactDeviceId;
    String lastContactFirstName;
    String lastContactLastName;
    Date lastUpdated;
    Date dateCreated;
    Date lastMessageDate;
    Date lastNonDeletedMessageDate;
    String lastContactMobileNumber;
    String lastMessageBody;

    public String getLastMessageBody() {
        return lastMessageBody;
    }

    public void setLastMessageBody(String lastMessageBody) {
        this.lastMessageBody = lastMessageBody;
    }

    public String getLastContactMobileNumber() {
        return lastContactMobileNumber;
    }

    public void setLastContactMobileNumber(String lastContactMobileNumber) {
        this.lastContactMobileNumber = lastContactMobileNumber;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(long deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceAddress() {
        return deviceAddress;
    }

    public void setDeviceAddress(String deviceAddress) {
        this.deviceAddress = deviceAddress;
    }

    public String getFingerprint() {
        return fingerprint;
    }

    public void setFingerprint(String fingerprint) {
        this.fingerprint = fingerprint;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getBcc() {
        return bcc;
    }

    public void setBcc(String bcc) {
        this.bcc = bcc;
    }

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public long getLastContactId() {
        return lastContactId;
    }

    public void setLastContactId(long lastContactId) {
        this.lastContactId = lastContactId;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public long getLastContactDeviceId() {
        return lastContactDeviceId;
    }

    public void setLastContactDeviceId(long lastContactDeviceId) {
        this.lastContactDeviceId = lastContactDeviceId;
    }

    public String getLastContactFirstName() {
        return lastContactFirstName;
    }

    public void setLastContactFirstName(String lastContactFirstName) {
        this.lastContactFirstName = lastContactFirstName;
    }

    public String getLastContactLastName() {
        return lastContactLastName;
    }

    public void setLastContactLastName(String lastContactLastName) {
        this.lastContactLastName = lastContactLastName;
    }

    public Date getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getLastMessageDate() {
        return lastMessageDate;
    }

    public void setLastMessageDate(Date lastMessageDate) {
        this.lastMessageDate = lastMessageDate;
    }

    public Date getLastNonDeletedMessageDate() {
        return lastNonDeletedMessageDate;
    }

    public void setLastNonDeletedMessageDate(Date lastNonDeletedMessageDate) {
        this.lastNonDeletedMessageDate = lastNonDeletedMessageDate;
    }
}
