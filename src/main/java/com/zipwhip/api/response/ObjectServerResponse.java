package com.zipwhip.api.response;

import com.zipwhip.api.signals.Signal;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Represents a JSON Object ServerResponse.
 */
public class ObjectServerResponse extends ServerResponse {

    public JSONObject response;

    public ObjectServerResponse(String raw, boolean success, JSONObject response, Map<String, Map<String, List<Signal>>> sessions) {
        super(raw, success, sessions);
        this.response = response;
    }

}
