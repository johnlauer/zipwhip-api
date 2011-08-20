package com.zipwhip;

import com.zipwhip.api.Connection;
import com.zipwhip.api.HttpConnection;
import com.zipwhip.api.ZipwhipNetworkSupport;
import com.zipwhip.api.dto.DeviceToken;
import com.zipwhip.api.dto.MessageToken;
import com.zipwhip.api.request.RequestBuilder;
import com.zipwhip.api.response.ServerResponse;
import com.zipwhip.api.subscriptions.Subscription;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 7/6/11
 * Time: 4:16 PM
 * <p/>
 * Trusted Vendors will use this API to gain access to accounts and do various system level requests.
 */
public class DefaultVendorClient extends ZipwhipNetworkSupport implements VendorClient {

    /**
     * This is only for Vendors
     *
     * @param address The address of the destination you want to send to. In this version make sure you're only sending to ptn:/ style addresses.
     * @param body    The body you want to send to the customer. We won't put on an advertisement since this is a system message.
     * @param
     * @return
     * @throws Exception
     */
    public List<MessageToken> sendVendorMessage(String address, String body) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("address", address);
        params.put("body", body);
        params.put("apiKey", ((HttpConnection)getConnection()).getAuthenticator().apiKey); // TODO figure this out

        return responseParser.parseMessageTokens(executeSync(VENDOR_MESSAGE_SEND, params));
    }

    @Override
    public Connection getConnection() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public DeviceToken getDeviceBySessionKey(String sessionKey) throws Exception {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public DeviceToken enrollDevice(String deviceAddress, Subscription subscription) throws Exception {
        List<Subscription> subscriptions = new ArrayList<Subscription>();

        subscriptions.add(subscription);

        return enrollDevice(deviceAddress, subscriptions);
    }

    public DeviceToken enrollDevice(String deviceAddress) throws Exception {
        return enrollDevice(deviceAddress, (List<Subscription>) null);
    }

    @Override
    public String lookupPhoneKey(String carrier, String carrierKey) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("carrier", carrier);
        params.put("carrierKey", carrierKey);

        return responseParser.parseString(executeSync(PHONE_LOOKUP, params));
    }


    /**
     * You must be authenticated as a Vendor to use this method. It will reject your request if you are authenticated as a user.
     * <p/>
     * 1. If this device does not exist, it will be created.
     * 2. The device will be subscribed to ExternalAPI if it is not already. (You will be given the secret, so that you can execute calls on behalf of the acct.)
     * 3. The device will be subscribed to the subscriptions you specify.
     * 4. If you do not provide SubscriptionSettings, the defaults will be used.
     *
     * @param deviceAddress The user account you want to modify.
     * @return The details of the modified account.
     * @throws Exception
     */
    public DeviceToken enrollDevice(String deviceAddress, List<Subscription> subscriptions) throws Exception {
        Map<String, Object> params = new HashMap<String, Object>();

        // the device we want to modify
        params.put("address", deviceAddress);
        // our API key (so they can verify our signature)
        params.put("apiKey", ((HttpConnection)getConnection()).getAuthenticator().apiKey); // TODO: figure this out.

        // the list of subscriptions we want to add to the device
        if (subscriptions != null) {
            for (Subscription subscription : subscriptions) {
                params.put("subscription", subscription.toString());
            }
        }

        return responseParser.parseDeviceToken(executeSync(USER_ENROLL, params));
    }

    @Override
    protected void onDestroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
