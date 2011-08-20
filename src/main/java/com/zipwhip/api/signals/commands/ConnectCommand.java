package com.zipwhip.api.signals.commands;

import com.zipwhip.util.Serializer;
import com.zipwhip.util.StringUtil;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 8/2/11
 * Time: 11:28 AM
 * <p/>
 * for the {action:CONNECT} command
 */
public class ConnectCommand extends Command implements Serializer<ConnectCommand> {

    private String clientId;

    public ConnectCommand(String clientId) {
        this.clientId = clientId;
    }

    public boolean isSuccessful() {
        return !StringUtil.isNullOrEmpty(clientId);
    }

    public String getClientId() {
        return clientId;
    }

    public String toString() {
        return serialize(this);
    }

    @Override
    public String serialize(ConnectCommand item) {
        if (StringUtil.isNullOrEmpty(item.getClientId())) {
            return "{action:CONNECT}";
        } else {
            return "{action:\"CONNECT\",clientId:\"" + item.getClientId() + "\"}";
        }
    }
}
