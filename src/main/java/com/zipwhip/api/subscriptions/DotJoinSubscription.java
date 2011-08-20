/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.zipwhip.api.subscriptions;

/**
 * @author Michael
 */
public class DotJoinSubscription extends Subscription {

    public DotJoinSubscription(String targetDeviceAddress) {
        super("DotJoin", "{deviceAddress:'" + targetDeviceAddress + "'}");
    }

}
