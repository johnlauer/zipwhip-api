package com.zipwhip.api;

import com.zipwhip.api.response.JsonResponseParser;
import com.zipwhip.api.response.ResponseParser;
import com.zipwhip.api.response.ServerResponse;
import com.zipwhip.api.response.StringServerResponse;
import com.zipwhip.api.signals.SignalProvider;
import com.zipwhip.executors.ParallelBulkExecutor;
import com.zipwhip.lifecycle.DestroyableBase;
import org.apache.log4j.Logger;

import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 7/5/11
 * Time: 8:26 PM
 * <p/>
 * A base class for future implementation to extend.
 * <p/>
 * It takes all the non-API specific stuff out of ZipwhipClient implementations.
 * <p/>
 * If some class wants to communicate with Zipwhip, then it needs to extend this class. This class gives functionality
 * that can be used to parse Zipwhip API. This naming convention was copied from Spring (JmsSupport) base class.
 */
public abstract class ZipwhipNetworkSupport extends DestroyableBase {

    public static final String CONTACT_LIST = "contact/list";
    public static final String CONTACT_DELETE = "contact/delete";
    public static final String CONTACT_SAVE = "contact/save";
    public static final String CONTACT_GET = "contact/get";
    public static final String PHONE_LOOKUP = "phone/lookup";
    public static final String MESSAGE_SEND = "message/send";
    public static final String VENDOR_MESSAGE_SEND = "vendor/message/send";
    public static final String MESSAGE_LIST = "message/list";
    public static final String MESSAGE_READ = "message/read";
    public static final String MESSAGE_DELETE = "message/delete";
    public static final String MESSAGE_GET = "message/get";
    public static final String DEVICE_SAVE = "device/save";
    public static final String DEVICE_GET = "device/get";
    public static final String DEVICE_LIST = "device/list";
    public static final String GROUP_SAVE = "group/save";
    public static final String DEVICE_DELETE = "device/delete";
    public static final String USER_ENROLL = "user/enroll";
    public static final String SESSION_UPDATE = "session/update";
    public static final String SIGNALS_DISCONNECT = "signals/disconnect";
    public static final String SIGNALS_CONNECT = "signals/connect";
    public static final String SIGNAL_SEND = "signal/send"; // TODO: verify this
    public static final String USER_SAVE = "user/save";
    public static final String GROUP_ADD_MEMBER = "group/addMember";
    public static final String SESSION_GET = "session/get";

    protected static final Logger logger = Logger.getLogger(ZipwhipNetworkSupport.class);

    protected SignalProvider signalProvider;
    protected Connection connection;
    protected ResponseParser responseParser;
    protected ParallelBulkExecutor executor = new ParallelBulkExecutor(ZipwhipNetworkSupport.class);

    public ZipwhipNetworkSupport() {
        setResponseParser(JsonResponseParser.getInstance());
        setConnection(new HttpConnection());

        link(executor);
    }

    public ZipwhipNetworkSupport(Connection connection) {
        this();
        this.connection = connection;
    }

    public ZipwhipNetworkSupport(Connection connection, SignalProvider signalProvider) {
        this(connection);
        this.signalProvider = signalProvider;
    }

    public ZipwhipNetworkSupport(SignalProvider signalProvider) {
        this();
        this.signalProvider = signalProvider;
    }

    protected ServerResponse executeSync(final String method, final Map<String, Object> params) throws Exception {
        return get(executeAsync(method, params));
    }

    protected Future<ServerResponse> executeAsync(final String method, final Map<String, Object> params) throws Exception {

        FutureTask<ServerResponse> task = new FutureTask<ServerResponse>(new Callable<ServerResponse>() {
            @Override
            public ServerResponse call() throws Exception {

                if (!connection.isAuthenticated()) {
                    throw new Exception("The connection is not authenticated, can't continue.");
                }

                // execute the request
                Future<String> future = connection.send(method, params);

                String response = future.get(45, TimeUnit.SECONDS);

                // parse the response
                ServerResponse serverResponse = responseParser.parse(response);

                // see if there are any signals we should announce
                announceSignals(serverResponse);

                // throw the error if the server returned false.
                checkAndThrowError(serverResponse);

                // return the successful response
                return serverResponse;
            }
        });

        executor.execute(task);

        return task;
    }

    protected void announceSignals(ServerResponse serverResponse) {
        if (serverResponse == null) {
            return;
        }
        if (serverResponse.sessions == null) {
            return;
        }
        if (getSignalProvider() == null) {
            return;
        }

//        if (getSignalProvider().getSignalReceivedCallback() == null){
//            return;
//        }

//        getSignalProvider().getSignalReceivedCallback().onSignalReceived(this, serverResponse.sessions);
        // todo FIX THIS?
    }

    void checkAndThrowError(ServerResponse serverResponse) throws Exception {
        if (serverResponse == null) {
            //throw new Exception("Server response object was null");
            // its ok if the server returns null, right?
            return;
        }
        if (!serverResponse.success) {
            throwError(serverResponse);
        }
    }

    void throwError(ServerResponse serverResponse) throws Exception {
        if (serverResponse instanceof StringServerResponse) {
            StringServerResponse string = (StringServerResponse) serverResponse;
            throw new Exception(string.response);
        } else {
            throw new Exception(serverResponse.raw);
        }
    }

    public SignalProvider getSignalProvider() {
        return signalProvider;
    }

    public void setSignalProvider(SignalProvider signalProvider) {
        this.signalProvider = signalProvider;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        if (this.connection != null) {
            unlink(this.connection);
        }
        this.connection = connection;
        link(this.connection);
    }

    public ResponseParser getResponseParser() {
        return responseParser;
    }

    public void setResponseParser(ResponseParser responseParser) {
        this.responseParser = responseParser;
    }

    protected <T> T get(Future<T> task) throws ExecutionException, TimeoutException, InterruptedException {
        return task.get(30, TimeUnit.SECONDS);
    }

    protected Future<Void> wrapVoid(final Future<?> future) {

        FutureTask<Void> task = new FutureTask<Void>(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                future.get();
                return null;
            }
        });

        executor.execute(task);

        return task;
    }

//    @Override
//    public void destroy() {
//        super.destroy();
//    }
}
