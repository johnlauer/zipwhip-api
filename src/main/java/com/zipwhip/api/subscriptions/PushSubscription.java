package com.zipwhip.api.subscriptions;

import org.json.me.JSONObject;


/**
 * Created by IntelliJ IDEA.
 * * Date: Jul 19, 2009
 * Time: 5:20:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class PushSubscription extends Subscription {

    public PushSubscription() {
        super("push", null);
    }

    public PushSubscription(String url) {
        this();
        this.setSignalFilters("/signal");
        this.setURL(url);
    }

    public void setURL(String url) {
        this.encodedSubscriptionSettings = "{url:" + JSONObject.quote(url) + "}";
    }

}
