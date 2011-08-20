package com.zipwhip.api.signals;

/**
 * Created by IntelliJ IDEA.
 * User: jed
 * Date: 6/24/11
 * Time: 4:47 PM
 * <p/>
 * An in-memory only solution is not recommended since it won't work for power cycle. In an android environment,
 * you will want to implement this via SharedPreferences, or MySql. In a Java app, you might want to use a File.
 * For easy testing or simple apps, just do this via Memory.
 * <p/>
 * A ClientId is a unqiue identifier that represents the TCP/IP connection to the SignalServer. The SignalServer will
 * keep track in persistent storage your sessionKey/clientId pairings, so that this clientId is registered to receive
 * the appropriate signals.
 */
public interface ClientIdManager {

    /**
     * @return the currently managed clientId or null
     */
    String getClientId();

    /**
     * @param clientId the clientId to be managed
     */
    void setClientId(String clientId);

}
