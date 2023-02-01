package pers.kagw.core;

import io.netty.handler.codec.http.FullHttpRequest;
import lombok.extern.slf4j.Slf4j;
import pers.kagw.core.channel.Channel;
import pers.kagw.core.channel.ComponentNode;
import pers.kagw.core.channel.NodeIterator;
import pers.kagw.core.dto.RequestHandlerDTO;
import pers.kagw.core.dto.ResourceDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.exception.ExceptionEnum;
import pers.kagw.core.handler.ChannelService;

import java.util.Objects;

/**
 * @author kwsc98
 */
@Slf4j
public class DisposeService {

    private final KagwApplicationContext kagwApplicationContext;


    public DisposeService(KagwApplicationContext kagwApplicationContext) {
        this.kagwApplicationContext = kagwApplicationContext;
    }

    public Object dealWith(RequestHandlerDTO<?> requestHandlerDTO) {
        ChannelService channelService = kagwApplicationContext.getChannelService();
        String url = requestHandlerDTO.getResourceUrl();
        ResourceDTO resourceDTO = channelService.getResource(url);
        if (Objects.isNull(resourceDTO)) {
            log.info("ResourceDTO Not Found");
            throw new ApiGateWayException(ExceptionEnum.RESOURCES_NOT_EXIST);
        }
        if (Objects.isNull(resourceDTO.getChannel())) {
            Channel channel = kagwApplicationContext
                    .getChannelService()
                    .getComponentChannel(resourceDTO.getGroupHandlerList(), resourceDTO.getHandlerList(), resourceDTO);
            resourceDTO.setChannel(channel);
        }
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
