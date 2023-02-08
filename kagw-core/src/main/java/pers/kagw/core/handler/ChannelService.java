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
import pers.kagw.core.dto.GroupDTO;
import pers.kagw.core.dto.InterfaceDTO;
import pers.kagw.core.dto.ResourceDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.handler.impl.OkHttpClientComponentHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author kwsc98
 */
@Slf4j
public class ChannelService {

    private final KagwApplicationContext kagwApplicationContext;

    private Trie<ResourceDTO> resourceTrie = new Trie<>();
    private ConcurrentHashMap<String, ResourceDTO> concurrentHashMap = new ConcurrentHashMap<>();

    private final OkHttpClientComponentHandler okHttpClientComponentHandler;

    public ChannelService(KagwApplicationContext kagwApplicationContext) {
        this.kagwApplicationContext = kagwApplicationContext;
        this.okHttpClientComponentHandler = new OkHttpClientComponentHandler();
    }

    public ResourceDTO getResource(String url) {
        ResourceDTO resourceDTO = this.concurrentHashMap.get(url);
        if (Objects.nonNull(resourceDTO)) {
            return resourceDTO;
        }
        resourceDTO = this.resourceTrie.get(splitUrl(url));
        if (Objects.nonNull(resourceDTO)) {
            this.concurrentHashMap.put(url, resourceDTO);
        }
        return resourceDTO;
    }


    public synchronized void registrationGroupList(List<GroupDTO> groupDTOList) {
        for (GroupDTO groupDTO : groupDTOList) {
            if (groupDTO.getTimeOut() == -1) {
                groupDTO.setTimeOut(3000);
            }
            List<InterfaceDTO> interfaceDTOList = groupDTO.getInterfaceDTOList();
            for (InterfaceDTO interfaceDTO : interfaceDTOList) {
                if (interfaceDTO.getTimeOut() == -1) {
                    interfaceDTO.setTimeOut(groupDTO.getTimeOut());
                }
            }
        }
        Trie<ResourceDTO> resourceTrie = new Trie<>();
        for (GroupDTO groupDTO : groupDTOList) {
            registrationGroup(resourceTrie, groupDTO);
        }
        this.resourceTrie = resourceTrie;
        this.concurrentHashMap = new ConcurrentHashMap<>();
    }

    public void registrationGroup(Trie<ResourceDTO> resourceTrie, GroupDTO groupDTO) {
        log.info("GroupDTO : {} Start Registration", groupDTO.getResourceName());
        LoadBalancer loadBalancer = groupDTO.getLoadBalancer();
        ResourceDTO groupResourceDTO = ResourceDTO.build().setBaseDTO(groupDTO).setLoadBalancer(loadBalancer).setHandlerList(groupDTO.getHandlerList());
        resourceTrie.put(splitUrl(groupDTO.getResourceUrl()), groupResourceDTO);
        List<InterfaceDTO> interfaceDTOList = groupDTO.getInterfaceDTOList();
        for (InterfaceDTO interfaceDTO : interfaceDTOList) {
            List<String> groupHandlerList = null;
            if (interfaceDTO.isGroupExtends()) {
                groupHandlerList = groupDTO.getHandlerList();
            }
            ResourceDTO interfaceResourceDTO = ResourceDTO.build().setBaseDTO(interfaceDTO).setLoadBalancer(loadBalancer).setRouteResourceUrl(interfaceDTO.getRouteResourceUrl()).setGroupHandlerList(groupHandlerList).setHandlerList(interfaceDTO.getHandlerList());
            resourceTrie.put(splitUrl(interfaceDTO.getResourceUrl()), interfaceResourceDTO);
        }
        log.info("GroupDTO : {} Registration Done", groupDTO.getResourceName());
    }

    public Channel getComponentChannel(ResourceDTO resourceDTO) {
        List<String> groupHandlerList = resourceDTO.getGroupHandlerList();
        List<String> interfacehandlerList = resourceDTO.getHandlerList();
        List<String> handlerList = new ArrayList<>();
        if (Objects.nonNull(groupHandlerList) && !groupHandlerList.isEmpty()) {
            handlerList.addAll(groupHandlerList);
        }
        if (Objects.nonNull(interfacehandlerList) && !interfacehandlerList.isEmpty()) {
            handlerList.addAll(interfacehandlerList);
        }
        List<ComponentNode> requestComponentNodeList = new ArrayList<>();
        List<ComponentNode> responseComponentNodeList = new ArrayList<>();
        ComponentNode baseComponentNode = ComponentNode.build().setHandler(this.okHttpClientComponentHandler).setConfigObject(resourceDTO);
        HandlerService handlerService = this.kagwApplicationContext.getHandlerService();
        for (String handlerStr : handlerList) {
            String[] handler = handlerStr.split(":", 2);
            ComponentHandler<?, ?> componentHandler = handlerService.getComponentHandler(handler[0]);
            if (Objects.nonNull(componentHandler)) {
                ComponentNode componentNode = ComponentNode.build().setHandler(componentHandler);
                if (handler.length > 1) {
                    componentNode.setConfigJsonStr(handler[1]);
                } else {
                    componentNode.setConfigJsonStr(null);
                }
                if (componentHandler instanceof RequestComponentHandler) {
                    requestComponentNodeList.add(componentNode);
                } else if (componentHandler instanceof ResponseComponentHandler) {
                    responseComponentNodeList.add(componentNode);
                } else if (componentHandler instanceof BaseComponentHandler) {
                    if(Objects.isNull(componentNode.getConfigObject())){
                        componentNode.setConfigObject(resourceDTO);
                    }
                    baseComponentNode = componentNode;
                }
            }
        }
        ComponentChannel componentChannel = new ComponentChannel();
        ChannelPipeline channelPipeline = componentChannel.pipeline();
        for (ComponentNode componentNode : requestComponentNodeList) {
            channelPipeline.addLast(componentNode);
        }
        channelPipeline.addLast(baseComponentNode);
        for (ComponentNode componentNode : responseComponentNodeList) {
            channelPipeline.addLast(componentNode);
        }
        return componentChannel;
    }

    private static List<String> splitUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            log.error("Url is Empty SplitUrl Error");
            throw new ApiGateWayException();
        }
        return Arrays.stream(url.split("/")).filter(StringUtils::isNotBlank).collect(Collectors.toList());
    }


}
