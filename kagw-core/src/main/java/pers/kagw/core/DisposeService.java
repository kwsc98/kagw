package pers.kagw.core;

import com.fasterxml.jackson.databind.util.JSONPObject;
import io.netty.handler.codec.http.FullHttpRequest;
import pers.kagw.core.channel.Channel;
import pers.kagw.core.channel.ComponentNode;
import pers.kagw.core.channel.NodeIterator;
import pers.kagw.core.handler.ChannelService;

/**
 * @author kwsc98
 */
public class DisposeService {

    private final KagwApplicationContext kagwApplicationContext;


    public DisposeService(KagwApplicationContext kagwApplicationContext) {
        this.kagwApplicationContext = kagwApplicationContext;
    }

    public Object dealWith(FullHttpRequest fullHttpRequest) {
        ChannelService channelService = kagwApplicationContext.getChannelService();
        String uri = fullHttpRequest.uri();
        Channel componentChannel = channelService.getChannel(uri);
        NodeIterator nodeIterator = componentChannel.pipeline().getIterator();
        return handle(fullHttpRequest, nodeIterator);
    }
    @SuppressWarnings("unchecked")
    private Object handle(Object object, NodeIterator nodeIterator) {
        if (nodeIterator.hasNext()) {
            ComponentNode componentNode = nodeIterator.next();
            object = componentNode.getHandler().handle(object);
            object = handle(object, nodeIterator);
        }
        return object;
    }


}
