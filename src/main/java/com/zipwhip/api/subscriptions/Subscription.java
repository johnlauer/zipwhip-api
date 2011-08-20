package com.zipwhip.api.subscriptions;

import org.json.me.JSONException;
import org.json.me.JSONObject;

/**
 * Created by IntelliJ IDEA.
 * * Date: Jul 19, 2009
 * Time: 5:21:03 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Subscription {

    public String signalFilters;
    public String subscriptionKey;
    public String encodedSubscriptionSettings;

    public Subscription(String subscriptionKey, String encodedSubscriptionSettings) {
        this.subscriptionKey = subscriptionKey;
        this.encodedSubscriptionSettings = encodedSubscriptionSettings;
    }


    public String getEncodedSubscriptionSettings() {
        return encodedSubscriptionSettings;
    }

    public void setEncodedSubscriptionSettings(String encodedSubscriptionSettings) {
        this.encodedSubscriptionSettings = encodedSubscriptionSettings;
    }

    public String getSignalFilters() {
        return signalFilters;
    }

    public void setSignalFilters(String signalFilters) {
        this.signalFilters = signalFilters;
    }

    public String getSubscriptionKey() {
        return subscriptionKey;
    }

    public void setSubscriptionKey(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }


    @Override
    public String toString() {

        JSONObject json = new JSONObject();

        try {
            json.put("subscriptionKey", subscriptionKey);
            json.put("encodedSubscriptionSettings", encodedSubscriptionSettings);
            json.put("signalFilters", signalFilters);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json.toString();
    }


}
