package com.zipwhip.api;

import com.zipwhip.api.dto.DeviceToken;
import com.zipwhip.api.response.JsonResponseParser;
import com.zipwhip.api.response.ResponseParser;
import com.zipwhip.api.response.ServerResponse;
import com.zipwhip.lib.SignTool;
import com.zipwhip.lifecycle.DestroyableBase;
import com.zipwhip.util.Factory;
import com.zipwhip.util.StringUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 7/7/11
 * Time: 2:12 PM
 * <p/>
 * Creates HttpConnection.
 */
public class HttpConnectionFactory implements Factory<Connection> {

    private ResponseParser responseParser = new JsonResponseParser();

    private String host = HttpConnection.DEFAULT_HOST;
    private String username;
    private String password;
    private String apiKey;
    private String secret;
    private String sessionKey;
    private boolean debug = true;

    /**
     * Creates a generic unauthenticated HttpConnection.
     *
     * @return
     * @throws Exception
     */
    @Override
    public Connection create() throws Exception {
        final HttpConnection connection = new HttpConnection();

        connection.setSessionKey(sessionKey);
        connection.setHost(host);
        connection.setDebug(debug);
        connection.setAuthenticator(new SignTool(apiKey, secret));

        if (!StringUtil.isNullOrEmpty(apiKey) && !StringUtil.isNullOrEmpty(secret)) {
            // good, the authenticator should be ready go to.
            if (StringUtil.isNullOrEmpty(sessionKey)) {
                // we need a sessionKey
                requestSessionKey(connection);
            }
        } else if (!StringUtil.isNullOrEmpty(username) && !StringUtil.isNullOrEmpty(password)) {
            // we have a username/password

            Map<String, Object> params = new HashMap<String, Object>();

            params.put("mobileNumber", username);
            params.put("password", password);

            Future<String> future = connection.send("login", params);
            ServerResponse serverResponse = responseParser.parse(future.get());
            DeviceToken token = responseParser.parseDeviceToken(serverResponse);

            connection.setAuthenticator(new SignTool(token.apiKey, token.secret));
            connection.setSessionKey(token.sessionKey);
        }

        return connection;
    }

    protected void requestSessionKey(final Connection connection) throws Exception {

        Map<String, Object> params = new HashMap<String, Object>();

        params.put("apiKey", apiKey);

        Future<String> future = connection.send(ZipwhipNetworkSupport.SESSION_GET, params);

        String sessionKey = responseParser.parseString(responseParser.parse(future.get()));

        if (StringUtil.isNullOrEmpty(sessionKey)) {
            throw new Exception("Retrieving a sessionKey failed");
        }

        connection.setSessionKey(sessionKey);
    }

//    protected ServerResponse execute(Callable<String> runnable) throws Exception {
//
//        // execute the request
//        String response = runnable.call();
//
//        // parse the response
//        ServerResponse serverResponse = responseParser.parse(response);
//
//        if (serverResponse == null) {
//            throw new Exception("The serverResponse was null");
//        }
//
//        if (!serverResponse.success) {
//            throw new Exception(serverResponse.raw);
//        }
//
//        // return the successful response
//        return serverResponse;
//    }

    public ResponseParser getResponseParser() {
        return responseParser;
    }

    public void setResponseParser(ResponseParser responseParser) {
        this.responseParser = responseParser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
