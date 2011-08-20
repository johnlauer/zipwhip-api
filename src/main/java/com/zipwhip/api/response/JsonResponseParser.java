package com.zipwhip.api.response;

import com.zipwhip.api.dto.*;
import com.zipwhip.api.signals.Signal;
import com.zipwhip.util.StringUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Date: Jul 18, 2009
 * Time: 10:22:28 AM
 * <p/>
 * Parses a ServerResponse from a Json string.
 */
public class JsonResponseParser implements ResponseParser {

    private static Logger logger = Logger.getLogger(JsonResponseParser.class);

    private static ResponseParser instance;
    private static final String EMPTY_OBJECT = "{}";

    private JsonDtoParser parser = JsonDtoParser.getInstance();

    /**
     * Parse out a ServerResponse from a
     *
     * @param response
     * @return
     * @throws JSONException
     */
    public ServerResponse parse(String response) throws JSONException {
        logger.debug(">>>>> " + response);
        System.out.println(">>>"  + response);

        if (StringUtil.isNullOrEmpty(response)) {
            return null;
        } else if (StringUtil.equalsIgnoreCase("null", response)) {
            return null;
        }

        JSONObject thing = new JSONObject(response);
        String responseKey = "response";

        boolean success = thing.getBoolean("success");
        Map<String, Map<String, List<Signal>>> sessions = null;

        JSONObject sessionsObject = thing.optJSONObject("sessions");
        if (sessionsObject != null && !EMPTY_OBJECT.equals(sessionsObject.toString())) {
//             logger.debug(":NOTNULL: " + sessionsObject);
            // we dont need to support this anymore?
//            sessions = parseSessionSignals(sessionsObject);
        } else {
//             logger.debug(":NULL: " + sessionsObject);
        }

        /// IS THIS A COMPLEX OBJECT?
        JSONObject jsonObject = thing.optJSONObject(responseKey);
        if (jsonObject != null) {
            return new ObjectServerResponse(response, success, jsonObject, sessions);
        }

        /// IS THIS AN ARRAY?
        JSONArray jsonArray = thing.optJSONArray(responseKey);
        if (jsonArray != null) {
            return new ArrayServerResponse(response, success, jsonArray, sessions);
        }

        /// IS THIS A STRING?
        String string = thing.optString(responseKey, null);
        if (string != null) {
            // a string
            return new StringServerResponse(response, success, string, sessions);
        }


        /// THIS MUST BE A BOOLEAN
        boolean bool = thing.getBoolean(responseKey);
        return new BooleanServerResponse(response, success, bool, sessions);
    }

    public List<MessageToken> parseMessageTokens(ServerResponse serverResponse) throws Exception {

        List<MessageToken> result = null;

        if (serverResponse instanceof ObjectServerResponse) {
            ObjectServerResponse r = (ObjectServerResponse) serverResponse;
            JSONArray array = r.response.getJSONArray("tokens");

            result = parser.parseMessageTokens(array);
        } else if (serverResponse instanceof ArrayServerResponse) {
            ArrayServerResponse cplx = (ArrayServerResponse) serverResponse;

            result = parser.parseMessageTokens(cplx.response);
        }

        return result;
    }

    public Message parseMessage(ServerResponse serverResponse) throws Exception {
        Message result = null;
        if (serverResponse instanceof ObjectServerResponse) {
            ObjectServerResponse cplx = (ObjectServerResponse) serverResponse;

            return parser.parseMessage(cplx.response);
        }
        return result;
    }

    public String parseString(ServerResponse serverResponse) throws Exception {
        String result = null;
        if (serverResponse instanceof StringServerResponse) {
            StringServerResponse stringServerResponse = (StringServerResponse) serverResponse;
            result = stringServerResponse.response;
        } else {
            throw new Exception("Unknown or unexpected server response");
        }
        return result;
    }

    public Contact parseContact(ServerResponse serverResponse) throws Exception {
        if (!(serverResponse instanceof ObjectServerResponse)) {
            return null;
        }

        ObjectServerResponse cplx = (ObjectServerResponse) serverResponse;

        return parser.parseContact(cplx.response);
    }

    public DeviceToken parseDeviceToken(ServerResponse serverResponse) throws Exception {
        DeviceToken result = null;
        if (serverResponse instanceof ObjectServerResponse) {
            ObjectServerResponse cplx = (ObjectServerResponse) serverResponse;
            result = new DeviceToken();
            result.device = new Device();

            System.out.println(cplx.response.toString());

            if (cplx.response.has("device")) {
                result.device.setAddress(cplx.response.getJSONObject("device").getString("address"));
                result.device.setDeviceNumber(cplx.response.getJSONObject("device").getInt("deviceId"));
                result.device.setId(cplx.response.getJSONObject("device").getLong("id"));
            } else {
                result.device.setAddress(cplx.response.getString("address"));
                result.device.setId(cplx.response.getLong("deviceId"));
                result.device.setDeviceNumber(cplx.response.getInt("deviceNumber"));
            }

            result.apiKey = cplx.response.getString("apiKey");
            result.secret = cplx.response.getString("secret");
            result.sessionKey = cplx.response.getString("sessionKey");
        }
        return result;
    }

    public synchronized static ResponseParser getInstance() {
        if (instance == null){
            instance = new JsonResponseParser();
        }
        return instance;
    }
}
