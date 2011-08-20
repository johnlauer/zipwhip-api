package com.zipwhip.api.response;

import com.zipwhip.api.signals.Signal;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * * Date: Jul 18, 2009
 * Time: 10:23:38 AM
 * To change this template use File | Settings | File Templates.
 */
public class StringServerResponse extends ServerResponse {
    public String response;

    public StringServerResponse(String raw, boolean success, String response, Map<String, Map<String, List<Signal>>> sessions) {
        super(raw, success, sessions);
        this.response = response;
    }
}
