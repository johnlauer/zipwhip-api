package com.zipwhip;

import com.zipwhip.api.Connection;
import com.zipwhip.api.dto.DeviceToken;
import com.zipwhip.api.dto.MessageToken;
import com.zipwhip.api.subscriptions.Subscription;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 7/5/11
 * Time: 7:56 PM
 * <p/>
 * Provides administrator tools for a Vendor
 */
public interface VendorClient {

    void setConnection(Connection connection);

    Connection getConnection();

    List<MessageToken> sendVendorMessage(String address, String body) throws Exception;

    DeviceToken enrollDevice(String deviceAddress, Subscription subscription) throws Exception;

    DeviceToken enrollDevice(String deviceAddress) throws Exception;

    DeviceToken enrollDevice(String deviceAddress, List<Subscription> subscriptions) throws Exception;

    DeviceToken getDeviceBySessionKey(String sessionKey) throws Exception;


    /**
     * Lookup a phone key by the carrierKey. The carrierKey is the phone info from that carrier's LDAP.
     *
     * @param carrier    Examples include Vzw, Tmo, Sprint
     * @param carrierKey The phoneKey as defined by the carrier. MOTO-z83 (this is proprietary per carrier)
     * @return The standardized Zipwhip phoneKey that maps to an image set.
     * @throws Exception
     */
    String lookupPhoneKey(String carrier, String carrierKey) throws Exception;

}
