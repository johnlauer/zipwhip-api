package com.zipwhip.api.signals;

/**
 * Created by IntelliJ IDEA.
 * User: jed
 * Date: 6/27/11
 * Time: 6:10 PM
 * <p/>
 * Simple in-memory clientId manager for use in testing.
 */
public class MemoryClientIdManager implements ClientIdManager {

    private String clientId;

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

}
