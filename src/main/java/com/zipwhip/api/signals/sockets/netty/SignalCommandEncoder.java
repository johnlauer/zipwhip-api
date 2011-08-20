package com.zipwhip.api.signals.sockets.netty;

import com.zipwhip.api.signals.commands.ConnectCommand;
import com.zipwhip.api.signals.commands.Command;
import com.zipwhip.api.signals.commands.Message;
import com.zipwhip.util.Serializer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

import java.util.HashMap;
import java.util.Map;

public class SignalCommandEncoder extends OneToOneEncoder implements ChannelHandler {

    Map<String, Serializer<? extends Command>> serializers = new HashMap<String, Serializer<? extends Command>>();

    public SignalCommandEncoder() {
        serializers.put(ConnectCommand.class.toString(), new ConnectCommand(null));
    }

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
        if (msg instanceof Command) {
            return serializeCommand(msg);
        }
        if (msg instanceof Message){
            return serializeCommand(((Message)msg).getCommand());
        }

        return null;
    }

    private Object serializeCommand(Object msg) {
        Command item = (Command) msg;

        Serializer serializer = serializers.get(item.getClass().toString());

        return serializer.serialize(item);
    }

}