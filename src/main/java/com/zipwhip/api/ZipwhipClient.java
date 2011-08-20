package com.zipwhip.api;


import com.zipwhip.api.dto.Contact;
import com.zipwhip.api.dto.Message;
import com.zipwhip.api.dto.MessageStatus;
import com.zipwhip.api.dto.MessageToken;
import com.zipwhip.api.response.ServerResponse;
import com.zipwhip.api.signals.SignalEvent;
import com.zipwhip.api.signals.SignalProvider;
import com.zipwhip.events.Observer;
import com.zipwhip.lib.Address;
import com.zipwhip.lifecycle.Destroyable;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

/**
 * This class operates on a Connection.
 *
 * @author Michael
 */
public interface ZipwhipClient extends Destroyable {

    List<MessageToken> sendMessage(Message message) throws Exception;

    List<MessageToken> sendMessage(Address address, String body) throws Exception;

    List<MessageToken> sendMessage(Address address, String body, String fromName) throws Exception;

    List<MessageToken> sendMessage(String address, String body) throws Exception;

    List<MessageToken> sendMessage(Collection<String> address, String body) throws Exception;

    List<MessageToken> sendMessage(Collection<String> address, String body, String fromName) throws Exception;

    List<MessageToken> sendMessage(Collection<String> address, String body, String fromName, String advertisement) throws Exception;

    List<MessageToken> sendMessage(String address, String body, String fromName, String advertisement) throws Exception;

    Contact saveGroup(String type, String advertisement) throws Exception;

    Contact saveGroup() throws Exception;

    Contact saveUser(Contact contact) throws Exception;

    /**
     * Returns a Message object
     *
     * @param uuid - message uuid
     * @return
     * @throws Exception
     */
    Message getMessage(String uuid) throws Exception;

    /**
     * Returns a MessageStatus object
     *
     * @param uuid - message uuid
     * @return
     * @throws Exception
     */
    MessageStatus getMessageStatus(String uuid) throws Exception;

    /**
     * Returns the contact for the provided contact id.
     *
     * @param id
     * @return contact
     * @throws Exception
     */
    Contact getContact(long id) throws Exception;

    /**
     * Returns the contact for the provided mobile number.
     *
     * @param mobileNumber
     * @return contact
     * @throws Exception
     */
    Contact getContact(String mobileNumber) throws Exception;

    void sendSignal(String scope, String channel, String event, String payload) throws Exception;

    void saveContact(String address, String firstName, String lastName, String phoneKey) throws Exception;

    void saveContact(String address, String firstName, String lastName, String phoneKey, String notes) throws Exception;

    Contact addMember(String groupAddress, String contactAddress) throws Exception;

    Contact addMember(String groupAddress, String contactAddress, String firstName, String lastName, String phoneKey, String notes) throws Exception;

    /**
     * Connect to Zipwhip Signals if setup.
     *
     * @throws Exception any connection problem
     * @return so you can wait until login succeeds
     */
    Future<Boolean> connect() throws Exception;

    /**
     * Listen for signals. This is a convenience method
     *
     * @param observer
     */
    void addSignalObserver(Observer<SignalEvent> observer);

    /**
     * Listen for connection changes. This is a convenience method
     *
     * This observer will be called if:
     *
     * We lose our TCP/IP connection to the SignalServer
     *
     * @param observer
     */
    void addSignalsConnectionObserver(Observer<Boolean> observer);


    /**
     * A connection to Zipwhip over a medium.
     *
     * @return
     */
    Connection getConnection();

    /**
     *
     * @param connection
     */
    void setConnection(Connection connection);

    /**
     * Allows you to receive signals.
     *
     * @return
     */
    SignalProvider getSignalProvider();

    /**
     * Setter for signalProvider
     *
     * @param client
     */
    void setSignalProvider(SignalProvider client);


}
