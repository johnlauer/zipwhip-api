package com.zipwhip.api.signals;

import com.zipwhip.api.signals.SignalProvider;
import com.zipwhip.api.signals.sockets.SocketSignalProvider;
import com.zipwhip.util.Factory;

/**
 * Created by IntelliJ IDEA.
 * User: Michael
 * Date: 8/12/11
 * Time: 6:54 PM
 *
 * Create signalProviders that connect via Sockets
 */
public class SocketSignalProviderFactory implements Factory<SignalProvider> {

    @Override
    public SignalProvider create() throws Exception {
        SocketSignalProvider result = new SocketSignalProvider();

        // todo: apply some settings

        return result;
    }

}
