package com.zipwhip.api.signals.commands;

import com.zipwhip.util.Parser;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 8/3/11
 * Time: 5:20 PM
 * <p/>
 * Parse out a SignalCommand from a String.
 */
public class JsonSignalCommandParser implements Parser<String, Command> {

    Map<String, Parser<JSONObject, Command>> parsers;

    public JsonSignalCommandParser() {
        parsers = new HashMap<String, Parser<JSONObject, Command>>();

        parsers.put("connect", new Parser<JSONObject, Command>() {

            @Override
            public Command parse(JSONObject object) throws Exception {
                String clientId = object.optString("clientId");

                return new ConnectCommand(clientId);
            }
        });

    }

    @Override
    public Command parse(String string) throws Exception {
        JSONObject json = new JSONObject(string);

        String action = json.optString("action");

        if (action != null) {
            action = action.toLowerCase();
        }

        Parser<JSONObject, Command> parser = parsers.get(action);

        if (parser == null) {
            throw new RuntimeException("The parser for " + action + " was not found.");
        }

        return parser.parse(json);
    }

}
