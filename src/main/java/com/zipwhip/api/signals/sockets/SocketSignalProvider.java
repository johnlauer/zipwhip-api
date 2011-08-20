package com.zipwhip.api.signals.sockets;

import com.zipwhip.api.signals.SignalConnection;
import com.zipwhip.api.signals.SignalEvent;
import com.zipwhip.api.signals.SignalProvider;
import com.zipwhip.api.signals.commands.ConnectCommand;
import com.zipwhip.api.signals.commands.Command;
import com.zipwhip.api.signals.sockets.netty.NettySignalConnection;
import com.zipwhip.events.ObservableHelper;
import com.zipwhip.events.Observer;
import com.zipwhip.lifecycle.DestroyableBase;
import com.zipwhip.util.StringUtil;

import java.util.concurrent.*;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 8/1/11
 * Time: 4:30 PM
 * <p/>
 * The SocketSignalProvider will connect to the Zipwhip SignalServer via TCP.
 * <p/>
 * This interface is intended to be used by 1 and only 1 ZipwhipClient object. This is a very high level interaction
 * where you connect for 1 user.
 */
public class SocketSignalProvider extends DestroyableBase implements SignalProvider {

    private ObservableHelper<Boolean> connectEvent = new ObservableHelper<Boolean>();
    private ObservableHelper<String> newClientIdEvent = new ObservableHelper<String>();
    private ObservableHelper<SignalEvent> signalEvent = new ObservableHelper<SignalEvent>();

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private String clientId = null;
    // so we can detect change.
    private String originalClientId = null;
    private SignalConnection connection = new NettySignalConnection();
    private CountDownLatch connectLatch = null;

    public SocketSignalProvider() {
        this(new NettySignalConnection());
    }

    public SocketSignalProvider(SignalConnection conn) {
        this.connection = conn;

        this.link(connection);
        this.link(connectEvent);
        this.link(newClientIdEvent);
        this.link(signalEvent);

        connection.onMessageReceived(new Observer<Command>() {
            /**
             * The NettySignalConnection will call this method when there's an event from
             * the remote signal server.
             *
             * @param sender The sender might not be the same object every time, so we'll let it just be object, rather than generics.
             * @param item - Rich object representing the notification.
             */
            @Override
            public void notify(Object sender, Command item) {
                if (item instanceof ConnectCommand) {
                    ConnectCommand command = (ConnectCommand) item;
                    if (command.isSuccessful()) {
                        // copy it over for stale checking
                        originalClientId = clientId;

                        clientId = command.getClientId();

                        if (!StringUtil.equals(clientId, originalClientId)) {
                            // not the same, lets announce
                            // announce on a separate thread
                            newClientIdEvent.notifyObservers(this, clientId);
                        }

                        if (connectLatch != null) {
                            // we need to countDown the latch, when it hits zero (after this call)
                            // the connect Future will complete. This gives the caller a way to block on our connection
                            connectLatch.countDown();
                        }
                    }
                }

//                if (item instanceof SignalSignalServerMessage){
//                    // we're being notified that we received a signal.
//                    SignalEvent signalEvent = new SignalEvent();
//                    signalEvent.setSessionKey(item.getSubscriptionId());
//                    signalEvent.setSignals(signals);
//                }

            }
        });
    }


    @Override
    public boolean isConnected() {
        return connection.isConnected() && !StringUtil.isNullOrEmpty(clientId);
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public Future<Boolean> connect(final String clientId) throws Exception {

        if (isConnected()) {
            throw new Exception("You are already connected");
        }

        // keep track of the original one, so we can detect change
        this.originalClientId = clientId;

        // this will help us do the connect synchronously
        connectLatch = new CountDownLatch(1);
        final Future connectFuture = connection.connect();

        FutureTask<Boolean> asdf = new FutureTask<Boolean>(new Callable<Boolean>() {

            @Override
            public Boolean call() {
                // TODO: notify callers somehow?

                try {
                    connectFuture.get(45, TimeUnit.SECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();

                }

                if (connection.isConnected()) {
                    connection.send(new ConnectCommand(clientId));

                    // block while the signal server is thinking/hanging.
                    try {
                        connectLatch.await(45, TimeUnit.SECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                final boolean result = isConnected();

                // queue it up, or else the "connect()" call will still block while the slow-ass observers fire.
                connectEvent.notifyObservers(this, result);

                return result;
            }
        });

        // this background thread stops us from blocking.
        executor.execute(asdf);

        return asdf;

    }

    @Override
    public Future<Boolean> connect() throws Exception {
        return connect(originalClientId);
    }

    @Override
    public Future<Void> disconnect() throws Exception {
        return connection.disconnect();
    }

    @Override
    public void onSignalReceived(Observer<SignalEvent> observer) {
        signalEvent.addObserver(observer);
    }

    @Override
    public void onConnectionChanged(Observer<Boolean> observer) {
        connectEvent.addObserver(observer);
    }

    @Override
    public void onNewClientIdReceived(Observer<String> observer) {
        newClientIdEvent.addObserver(observer);
    }


    @Override
    protected void onDestroy() {
        executor.shutdownNow(); // will return a list of runnables that have not fired yet, but we dont care.
    }
}
