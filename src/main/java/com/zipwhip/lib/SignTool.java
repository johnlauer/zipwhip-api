package com.zipwhip.lib;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by IntelliJ IDEA.
 * Date: Jul 18, 2009
 * Time: 4:34:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class SignTool {

    private javax.crypto.Mac mac = null;
    public String apiKey = null;

    public SignTool() {
    }

    public SignTool(String APIKey, String secret) throws Exception {
        this.setSecret(secret);
        this.setApiKey(APIKey);
    }

    public void setApiKey(String APIKey) {
        this.apiKey = APIKey;
    }

    // This method converts AWSSecretKey into crypto instance.
    public void setSecret(String secret) throws Exception {
        if (secret == null){
            mac = null;
        } else {
            mac = Mac.getInstance("HmacSHA1");
            byte[] keyBytes = secret.getBytes("UTF-8");
            mac.init(new SecretKeySpec(keyBytes, "HacSHA1"));
        }

    }

    // This method creates S3 signature for a given String.
    public String sign(String data) throws Exception {
        if (mac == null){
            return null;
        }

        // Signed String mst be a BASE64 encoded.
        return encodeBase64(mac.doFinal(data.getBytes("UTF8")));
    }

    public String encodeBase64(byte[] data) {
        String base64 = new sun.misc.BASE64Encoder().encodeBuffer(data);
        if (base64.endsWith("\r\n")) base64 = base64.substring(0, base64.length() - 2);
        return base64;
    }

}
