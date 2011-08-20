package com.zipwhip.api.response;

import com.zipwhip.api.signals.Signal;

import java.util.List;
import java.util.Map;

/**
 * Represents a ServerResponse that might be in some unknown format. We don't know if it's JSON or XML or binary encoded.
 */
public abstract class ServerResponse {

    public boolean success;

    public Map<String, Map<String, List<Signal>>> sessions;
    public String raw;

    public ServerResponse(String raw, boolean success, Map<String, Map<String, List<Signal>>> sessions) {
        this.raw = raw;
        this.success = success;
        this.sessions = sessions;
    }

}
