package pers.kagw.core.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import pers.kagw.core.common.LoadBalancer;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author kwsc98
 */
public abstract class ComponentHandler<T, C> {

    public abstract Object handle(T object, C config);

    public C init(String configStr) {
        return null;
    }

}
