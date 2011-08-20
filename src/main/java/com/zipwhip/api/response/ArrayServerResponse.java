package com.zipwhip.api.response;

import com.zipwhip.api.signals.Signal;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * Date: Jul 20, 2009
 * Time: 5:41:19 PM
 * <p/>
 * Represents an Array of data.
 */
public class ArrayServerResponse extends ServerResponse {
    public JSONArray response;

    public ArrayServerResponse(String raw, boolean success, JSONArray response, Map<String, Map<String, List<Signal>>> sessions) {
        super(raw, success, sessions);
        this.response = response;
    }
}
