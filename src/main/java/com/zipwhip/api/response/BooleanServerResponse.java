package com.zipwhip.api.response;

import com.zipwhip.api.signals.Signal;

import java.util.List;
import java.util.Map;

public class BooleanServerResponse extends ServerResponse {
    public boolean response;

    public BooleanServerResponse(String raw, boolean success, boolean response, Map<String, Map<String, List<Signal>>> sessions) {
        super(raw, success, sessions);
        this.response = response;
    }
}
