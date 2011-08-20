package com.zipwhip.api.signals;

import com.zipwhip.api.signals.commands.Command;
import com.zipwhip.events.Observer;
import com.zipwhip.lifecycle.Destroyable;

import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 8/2/11
 * Time: 11:48 AM
 * <p/>
 * Encapsulates a connection to the signal server. This is a very LOW LEVEL interface for talking with the SignalServer.
 * <p/>
 * It's not really intended for the end-customer to interact with this api.
 */
public interface SignalConnection extends Destroyable {

    /**
     * Initiate a raw TCP connection to the signal server ASYNCHRONOUSLY. This is just a raw connection, not
     * an authenticated/initialized one.
     *
     * @return The future will tell you when the connection is complete.
     * @throws Exception
     */
    Future<Boolean> connect() throws Exception;

    /**
     * Kill the TCP connection to the SignalServer ASYNCHRONOUSLY
     *
     * @return The future will tell you when the connection is terminated
     * @throws Exception
     */
    Future<Void> disconnect() throws Exception;

    /**
     * Send something to the SignalServer
     *
     * @param command
     */
    void send(Command command);

    /**
     * Determines if the socket is currently connected
     *
     * @return returns true if connected, false if not connected.
     */
    boolean isConnected();

    /**
     * Allows you to listen for things that are received by the API.
     *
     * @param observer
     * @return something you can observe
     */
    void onMessageReceived(Observer<Command> observer);

    /**
     * Allows you to observe the connection changes
     *
     * @param observer
     * @return something you can observe
     */
    void onConnectionStateChanged(Observer<Boolean> observer);

}
