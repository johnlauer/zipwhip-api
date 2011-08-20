package com.zipwhip.api.response;

import com.zipwhip.api.dto.*;
import com.zipwhip.lib.DateUtil;
import com.zipwhip.lib.JsonParserUtil;
import com.zipwhip.util.StringUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 7/6/11
 * Time: 3:04 PM
 * <p/>
 * Centralizes the act of parsing DTO's from JSON.
 */
public class JsonDtoParser {

    private static JsonDtoParser instance;

    public static JsonDtoParser getInstance() {
        if (instance == null) {
            instance = new JsonDtoParser();
        }

        return instance;
    }


    private Device parseDeviceContent(JSONObject content) {

        if (content == null) {
            return null;
        }

        Device device = new Device();

        device.setId(content.optInt("id"));
        device.setUuid(content.optString("uuid"));
        device.setAddress(content.optString("address"));
        device.setThread(content.optString("thread"));
        device.setVersion(content.optInt("version"));
        device.setLastUpdated(DateUtil.safeParseJsonDate(content.optString("lastUpdated")));
        device.setDateCreated(DateUtil.safeParseJsonDate(content.optString("dateCreated")));
        device.setUserId(content.optLong("userId"));
        device.setChannel(content.optString("channel"));
        device.setTextline(content.optString("textline"));
        device.setDisplayName(content.optString("displayName"));

        return device;
    }

    private Object parseMessageProgressContent(JSONObject content) {

        if (content == null) {
            return null;
        }

        MessageProgress messageProgress = new MessageProgress();

        int valueInt = content.optInt("value", Integer.MIN_VALUE);

        if (valueInt != Integer.MIN_VALUE) {

            messageProgress.setCode(valueInt);

        } else {

            JSONObject value = content.optJSONObject("value");

            if (value != null) {
                messageProgress.setDesc(value.optString("desc"));
                messageProgress.setCode(value.optInt("code"));
            }
        }

        messageProgress.setKey(content.optString("key"));

        return messageProgress;
    }

    private Object parseMessageContent(JSONObject content) {

        if (content == null) {
            return null;
        }

        Message message = new Message();

        message.setUuid(content.optString("uuid"));
        message.setDeviceId(content.optLong("deviceId"));
        message.setContactId(content.optLong("contactId"));
        message.setContactDeviceId(content.optLong("contactDeviceId"));
        message.setAddress(content.optString("address"));
        message.setRead(content.optBoolean("isRead"));
        message.setDeleted(content.optBoolean("deleted"));
        message.setFingerprint(content.optString("fingerprint"));
        message.setCc(content.optString("cc"));
        message.setBcc(content.optString("bcc"));
        message.setErrorState(content.optBoolean("errorState"));
        message.setBody(StringUtil.stripStringNull(content.optString("body")));
        message.setDateCreated(DateUtil.safeParseJsonDate(content.optString("dateCreated")));
        message.setLastUpdated(DateUtil.safeParseJsonDate(content.optString("lastUpdated")));
        message.setSourceAddress(content.optString("sourceAddress"));
        message.setDestAddress(content.optString("destAddress"));
        message.setStatusCode(content.optInt("statusCode"));
        message.setStatusDesc(content.optString("statusDesc"));
        message.setThread(content.optString("thread"));
        message.setChannel(content.optString("channel"));
        message.setFwd(content.optString("fwd"));
        message.setCarrier(content.optString("carrier"));
        message.setSubject(content.optString("subject"));
        message.setTo(content.optString("to"));
        message.setMobileNumber(content.optString("mobileNumber"));
        message.setFirstName(content.optString("firstName"));
        message.setLastName(content.optString("lastName"));
        message.setVersion(content.optLong("version"));

        return message;
    }

    private Object parseConversationContent(JSONObject content) {

        if (content == null) {
            return null;
        }

        Conversation conversation = new Conversation();

        conversation.setId(content.optInt("id"));
        conversation.setDeviceId(content.optLong("deviceId"));
        conversation.setDeviceAddress(content.optString("deviceAddress"));
        conversation.setFingerprint(content.optString("fingerprint"));
        conversation.setAddress(content.optString("address"));
        conversation.setCc(content.optString("cc"));
        conversation.setBcc(content.optString("bcc"));
        conversation.setUnreadCount(content.optInt("unreadCount"));
        conversation.setLastContactId(content.optLong("lastContactId"));
        conversation.setNew(content.optBoolean("new"));
        conversation.setDeleted(content.optBoolean("deleted"));
        conversation.setVersion(content.optInt("version"));
        conversation.setLastContactDeviceId(content.optLong("lastContactDeviceId"));
        conversation.setLastMessageBody(StringUtil.stripStringNull(content.optString("lastMessageBody")));
        conversation.setLastContactFirstName(StringUtil.stripStringNull(content.optString("lastContactFirstName")));
        conversation.setLastContactLastName(StringUtil.stripStringNull(content.optString("lastContactLastName")));
        conversation.setLastContactMobileNumber(StringUtil.stripStringNull(content.optString("lastContactMobileNumber")));

        conversation.setLastMessageDate(DateUtil.safeParseJsonDate(content.optString("lastMessageDate")));
        conversation.setLastNonDeletedMessageDate(DateUtil.safeParseJsonDate(content.optString("lastNonDeletedMessageDate")));

        return conversation;
    }


    private Object parseContactContent(JSONObject content) {

        if (content == null) {
            return null;
        }

        Contact contact = parseBasicDto(new Contact(), content);

        contact.setDeviceId(content.optLong("deviceId"));
        contact.setAddress(content.optString("address"));
        contact.setMobileNumber(content.optString("mobileNumber"));

        contact.setState(content.optString("state"));
        contact.setCity(content.optString("city"));
        contact.setId(content.optLong("id"));
        contact.setPhoneKey(content.optString("phoneKey"));

        contact.setThread(content.optString("thread"));
        contact.setFwd(content.optString("fwd"));
        contact.setCarrier(content.optString("carrier"));

        contact.setFirstName(content.optString("firstName"));
        contact.setLastName(content.optString("lastName"));

        contact.setMoCount(content.optInt("MOCount"));
        contact.setZoCount(content.optInt("ZOCount"));

        contact.setZipcode(content.optString("zipcode"));
        contact.setLatlong(content.optString("latlong"));
        contact.setEmail(content.optString("email"));
        contact.setNotes(content.optString("notes"));
        contact.setChannel(content.optString("channel"));

        return contact;
    }

    /**
     * Parse out some common attributes from the json.
     *
     * @param dto
     * @param content
     * @param <T>
     * @return
     */
    private <T extends BasicDto> T parseBasicDto(T dto, JSONObject content) {
        dto.setLastUpdated(DateUtil.safeParseJsonDate(content.optString("lastUpdated")));
        dto.setDateCreated(DateUtil.safeParseJsonDate(content.optString("dateCreated")));
        dto.setVersion(content.optInt("version"));

        return dto;
    }

    public Contact parseContact(JSONObject response) throws Exception {
        Contact result = new Contact();

        //portalId: not available
        result.setId(response.optLong("id")); //zipwhipId
        result.setAddress(response.optString("address")); //address
        result.setMobileNumber(response.getString("mobileNumber")); //mobileNumber
        result.setFirstName(response.getString("firstName")); //firstName
        result.setLastName(response.getString("lastName")); //lastName
        result.setPhone(response.getString("notes")); //phone
        result.setDateCreated(JsonParserUtil.getDate(response.getString("dateCreated"))); //dateCreated
        result.setLastUpdated(JsonParserUtil.getDate(response.getString("lastUpdated"))); //lastUpdated

        return result;
    }

    public Message parseMessage(JSONObject response) throws JSONException {
        Message result = new Message();

        //portalId: not available
        result.setContactId(response.getLong("from"));
        result.setId(response.getLong("id"));
        result.setUuid(response.getString("uuid"));
        result.setAddress(response.getString("address"));
        result.setRead(response.getBoolean("isRead"));
        result.setStatusCode(response.getInt("statusCode"));
        result.setStatusDesc(response.getString("statusDesc"));
        result.setBody(response.getString("body")); //body
        result.setMessageType(response.getString("type")); //messageType

        return result;
    }

    public List<MessageToken> parseMessageTokens(JSONArray array) throws JSONException {

        List<MessageToken> result = new ArrayList<MessageToken>();
        int len = array.length();
        for (int i = 0; i < len; i++) {

            JSONObject json = array.getJSONObject(i);
            MessageToken token = new MessageToken();

            token.message = json.getString("message");
            token.deviceId = json.optLong("device"); // will be 0 if it is a self message
            token.contactId = json.optLong("contact"); // will be 0 if it is a self message

            result.add(token);
        }

        return result;
    }

    public Conversation parseConversation(JSONObject content) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Device parseDevice(JSONObject content) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public Device parseMessageProgress(JSONObject content) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

}
