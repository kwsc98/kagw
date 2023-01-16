package pers.kagw.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pers.kagw.core.KagwApplicationContext;
import pers.kagw.core.channel.Channel;
import pers.kagw.core.channel.ChannelPipeline;
import pers.kagw.core.channel.ComponentChannel;
import pers.kagw.core.channel.ComponentNode;
import pers.kagw.core.common.LoadBalancer;
import pers.kagw.core.common.Trie;
import pers.kagw.core.common.WrrSmoothImpl;
import pers.kagw.core.dto.BaseDTO;
import pers.kagw.core.dto.GroupDTO;
import pers.kagw.core.dto.InterfaceDTO;
import pers.kagw.core.dto.ResourceDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.handler.impl.NettyClientComponentHandler;
import pers.kagw.core.handler.impl.OkHttpClientComponentHandler;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kwsc98
 */
@Slf4j
public class ChannelService {

    private final KagwApplicationContext kagwApplicationContext;

    private final Trie<ResourceDTO> resourceTrie;

    private final OkHttpClientComponentHandler okHttpClientComponentHandler;

    public ChannelService(KagwApplicationContext kagwApplicationContext) {
        this.resourceTrie = new Trie<>();
        this.kagwApplicationContext = kagwApplicationContext;
        this.okHttpClientComponentHandler = new OkHttpClientComponentHandler();
    }

    public ResourceDTO getResource(String url) {
        return this.resourceTrie.get(splitUrl(url));
    }


    public synchronized void registrationGroup(GroupDTO groupDTO) {
        log.info("GroupDTO :{} Start Registration", groupDTO.getResourceName());
        LoadBalancer loadBalancer = groupDTO.getLoadBalancer();
        ResourceDTO groupResourceDTO = ResourceDTO.build().setBaseDTO(groupDTO).setLoadBalancer(loadBalancer);
        Channel groupChannel = getComponentChannel(null, groupDTO.getHandlerList(), groupResourceDTO);
        groupResourceDTO.setChannel(groupChannel);
        this.resourceTrie.put(splitUrl(groupDTO.getResourceUrl()), groupResourceDTO);
        List<InterfaceDTO> interfaceDTOList = groupDTO.getInterfaceDTOList();
        for (InterfaceDTO interfaceDTO : interfaceDTOList) {
            List<String> groupHandlerList = null;
            if (interfaceDTO.isGroupExtends()) {
                groupHandlerList = groupDTO.getHandlerList();
            }
            ResourceDTO interfaceResourceDTO = ResourceDTO.build().setBaseDTO(groupDTO).setLoadBalancer(loadBalancer).setRouteResourceUrl(interfaceDTO.getResourceUrl());
            Channel interfaceChannel = getComponentChannel(groupHandlerList, groupDTO.getHandlerList(), interfaceResourceDTO);
            interfaceResourceDTO.setChannel(interfaceChannel);
            this.resourceTrie.put(splitUrl(interfaceDTO.getResourceUrl()), interfaceResourceDTO);

        }
        log.info("GroupDTO :{} Registration Done", groupDTO.getResourceName());
    }

    private Channel getComponentChannel(List<String> groupHandlerList, List<String> interfacehandlerList, ResourceDTO resourceDTO) {
        List<String> handlerList = new ArrayList<>();
        if (Objects.nonNull(groupHandlerList) && !groupHandlerList.isEmpty()) {
            handlerList.addAll(groupHandlerList);
        }
        if (Objects.nonNull(interfacehandlerList) && !interfacehandlerList.isEmpty()) {
            handlerList.addAll(interfacehandlerList);
        }
        List<ComponentNode> requestComponentNodeList = new ArrayList<>();
        List<ComponentNode> responseComponentNodeList = new ArrayList<>();
        HandlerService handlerService = this.kagwApplicationContext.getHandlerService();
        for (String handlerStr : handlerList) {
            String[] handler = handlerStr.split(":", 2);
            ComponentHandler<Object, Object> componentHandler = handlerService.getComponentHandler(handler[0]);
            if (Objects.nonNull(componentHandler)) {
                ComponentNode componentNode = ComponentNode.build().setHandler(componentHandler).setConfigJsonStr(handler[0]);
                if (handler.length > 1) {
                    componentNode.setConfigJsonStr(handler[1]);
                }
                if (componentHandler instanceof RequestComponentHandler) {
                    requestComponentNodeList.add(componentNode);
                } else if (componentHandler instanceof ResponseComponentHandler) {
                    responseComponentNodeList.add(componentNode);
                }
            }
        }
        ComponentChannel componentChannel = new ComponentChannel();
        ChannelPipeline channelPipeline = componentChannel.pipeline();
        for (ComponentNode componentNode : requestComponentNodeList) {
            channelPipeline.addLast(componentNode);
        }
        ComponentNode nettyClientComponentNode = ComponentNode.build().setHandler(this.okHttpClientComponentHandler).setConfigObject(resourceDTO);
        channelPipeline.addLast(nettyClientComponentNode);
        for (ComponentNode componentNode : responseComponentNodeList) {
            channelPipeline.addLast(componentNode);
        }
        return componentChannel;
    }

    private List<String> splitUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            log.error("Url is Empty SplitUrl Error");
            throw new ApiGateWayException();
        }
        return Arrays.stream(url.split("/")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }


}
