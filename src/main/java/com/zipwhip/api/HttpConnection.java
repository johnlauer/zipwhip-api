package com.zipwhip.api;

import com.zipwhip.api.request.RequestBuilder;
import com.zipwhip.lib.SignTool;
import com.zipwhip.lib.DownloadURL;
import com.zipwhip.lifecycle.DestroyableBase;
import com.zipwhip.util.StringUtil;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.*;

/**
 * * Date: Jul 17, 2009
 * Time: 8:34:02 PM
 * <p/>
 * Provides a persistent connection to a User on Zipwhip.
 * <p/>
 * You initialize this class with a sessionKey and then can execute raw requests on behalf of the user. If you want a more
 * Object oriented way to interact with Zipwhip, use Consumer instead of Connection.
 * <p/>
 * This class is thread safe.
 */
public class HttpConnection extends DestroyableBase implements Connection {

    public static final String DEFAULT_HOST = "http://network.zipwhip.com";

    private Logger logger = Logger.getLogger(HttpConnection.class);
    private String apiVersion = "/api/v1/";
    private String host = DEFAULT_HOST;
    private String sessionKey;
    private SignTool authenticator;
    private boolean debug = true;
    private ExecutorService executor = Executors.newCachedThreadPool();

    public HttpConnection() {

    }

    public HttpConnection(String apiKey, String secret) throws Exception {
        this(new SignTool(apiKey, secret));
    }

    public HttpConnection(SignTool authenticator) {
        this();
        this.setAuthenticator(authenticator);
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public boolean getDebug() {
        return this.debug;
    }

    public void setAuthenticator(SignTool authenticator) {
        this.authenticator = authenticator;
    }

    public SignTool getAuthenticator() {
        return this.authenticator;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    @Override
    public boolean isAuthenticated() {
        return !StringUtil.isNullOrEmpty(sessionKey);
    }

    @Override
    public boolean isConnected() {
        return true;
    }

    private String getUrl(String method) {
        return host + apiVersion + method;
    }

    /**
     * Send a request across Zipwhip and get the result back. No parsing is done, this is a low level transport.
     *
     * @param method each method has a name. example: user/get
     * @param params
     * @return
     */
    @Override
    public Future<String> send(String method, Map<String, Object> params) {
        RequestBuilder rb = new RequestBuilder();

        // convert the map into a key/value HTTP params string
        rb.params(params);

        return send(method, rb.build());
    }

    private Future<String> send(final String method, final String params){
        // put them together to form the full url
        FutureTask<String> task = new FutureTask<String>(new Callable<String>() {
            @Override
            public String call() throws Exception {
                // this is the base url+api+method
                final String url = getUrl(method);

                // this is the querystring part
                return DownloadURL.get(url + sign(method, params)); //TODO: replace with Ning. (netty http client)
            }
        });

        // execute this webcall async
        executor.execute(task);

        return task;
    }

    private String sign(String method, String params) throws Exception {
        String result = params;
        String connector = "&";

        if (StringUtil.isNullOrEmpty(result)) {
            connector = "?";
        }

        if (!StringUtil.isNullOrEmpty(sessionKey)) {
            result += connector + "session=" + sessionKey;
            connector = "&";
        }

        result += connector + "date=" + System.currentTimeMillis();
        String url = apiVersion + method + result;

        String signature = getSignature(url);

        if (signature != null && !signature.isEmpty()) {
            result += "&signature=" + signature;
        }


        url = host + apiVersion + method + result;

        if (this.debug) {
            logger.debug("Signed: " + url);
        }

        return result;
    }

    private String getSignature(String url) throws Exception {
        if (this.authenticator == null) {
            return null;
        }

        String result = this.authenticator.sign(url);
        if (this.debug) {
            logger.debug("Signing: " + url);
            System.out.println("Signing: " + url);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        // it returns a list of runnables that haven't executed, but we don't care...
        executor.shutdownNow();
    }
}
