package pers.kagw.core;

import io.netty.handler.codec.http.FullHttpRequest;
import pers.kagw.core.channel.Channel;
import pers.kagw.core.channel.ComponentNode;
import pers.kagw.core.channel.NodeIterator;
import pers.kagw.core.dto.RequestHandlerDTO;
import pers.kagw.core.dto.ResourceDTO;
import pers.kagw.core.handler.ChannelService;

/**
 * @author kwsc98
 */
public class DisposeService {

    private final KagwApplicationContext kagwApplicationContext;


    public DisposeService(KagwApplicationContext kagwApplicationContext) {
        this.kagwApplicationContext = kagwApplicationContext;
    }

    public Object dealWith(RequestHandlerDTO<?> requestHandlerDTO) {
        ChannelService channelService = kagwApplicationContext.getChannelService();
        String url = requestHandlerDTO.getResourceUrl();
        ResourceDTO resourceDTO = channelService.getResource(url);
        Channel componentChannel = resourceDTO.getChannel();
        NodeIterator nodeIterator = componentChannel.pipeline().getIterator();
        return handle(requestHandlerDTO, nodeIterator);
    }

    private Object handle(Object object, NodeIterator nodeIterator) {
        if (nodeIterator.hasNext()) {
            ComponentNode componentNode = nodeIterator.next();
            object = componentNode.getHandler().handle(object, componentNode.getConfigObject());
            object = handle(object, nodeIterator);
        }
        return object;
    }


}
