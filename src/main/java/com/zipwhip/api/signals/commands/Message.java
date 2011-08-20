package com.zipwhip.api.signals.commands;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 8/19/11
 * Time: 4:37 PM
 *
 * This is the basic envelope of a SignalServer packet.
 */
public class Message {

    // That's the body
    private Command command;
    private Address address;
    private Headers headers;

    public Message() {

    }

    public Message(Command command, Address address, Headers headers) {
        this.command = command;
        this.address = address;
        this.headers = headers;
    }

    public Message(Command command) {
        this.command = command;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Headers getHeaders() {
        return headers;
    }

    public void setHeaders(Headers headers) {
        this.headers = headers;
    }
}
