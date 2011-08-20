package com.zipwhip.api.signals;

import com.zipwhip.events.Observable;
import com.zipwhip.events.Observer;

import javax.security.auth.Destroyable;
import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 8/1/11
 * Time: 4:22 PM
 */
public interface SignalProvider extends Destroyable {

    /**
     * Determines if the SignalProvder is connected or not.
     *
     * @return
     */
    boolean isConnected();

    /**
     * The SignalServer uses a separate id to track you, because it's an Id given to a TCP/IP connection, not a user.
     *
     * @return the id that the SignalServer has for us
     */
    String getClientId();

    /**
     * Set the clientId. NOTE: This will only have an effect if you set it BEFORE calling connect.
     * @param clientId
     */
    void setClientId(String clientId);

    /**
     * Tell it to connect.
     *
     * @param clientId Pass in null if you dont have one.
     * @return A future that tells you when the connecting is complete. The string result is the clientId.
     * @throws Exception
     */
    Future<Boolean> connect(String clientId) throws Exception;

    /**
     * Tell it to connect.
     *
     * @return
     * @throws Exception
     */
    Future<Boolean> connect() throws Exception;

    /**
     * Tell it to disconnect.
     *
     * @return an event that tells you its complete
     * @throws Exception
     */
    Future<Void> disconnect() throws Exception;

    /**
     * You can Observe this event to capture things that come thru
     *
     * @param observer
     * @return
     */
    void onSignalReceived(Observer<SignalEvent> observer);

    /**
     * Observe the changes in connection. This is when your clientId is used for the first time.
     *
     * This is a low level TCP connection observable.
     *
     * @param observer
     * @return
     */
    void onConnectionChanged(Observer<Boolean> observer);

    /**
     * Observe when we authenticate with the SignalServer. This means a new clientId has been given to
     * us for the first time. This should only fire once.
     *
     * @param observer
     * @return
     */
    void onNewClientIdReceived(Observer<String> observer);

}
