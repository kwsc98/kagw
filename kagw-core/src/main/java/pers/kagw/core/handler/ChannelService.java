package pers.kagw.core.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import pers.kagw.core.KagwApplicationContext;
import pers.kagw.core.channel.Channel;
import pers.kagw.core.channel.ChannelPipeline;
import pers.kagw.core.channel.ComponentChannel;
import pers.kagw.core.channel.ComponentNode;
import pers.kagw.core.common.Trie;
import pers.kagw.core.dto.GroupDTO;
import pers.kagw.core.dto.InterfaceDTO;
import pers.kagw.core.exception.ApiGateWayException;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kwsc98
 */
@Slf4j
public class ChannelService {

    private KagwApplicationContext kagwApplicationContext;

    private Map<String, Channel> channelMap;

    private Trie<Channel> channelTrie;

    public ChannelService(KagwApplicationContext kagwApplicationContext) {
        this.channelMap = new HashMap<>();
        this.channelTrie = new Trie<>();
        this.kagwApplicationContext = kagwApplicationContext;
    }

    public Channel getChannel(String url) {
        return this.channelMap.get(url);
    }


    public synchronized void registrationGroup(GroupDTO groupDTO) {
        log.info("GroupDTO :{} Start Registration", groupDTO.getGroupName());
        List<String> keys = splitUrl(groupDTO.getPreUrl());

        log.info("GroupDTO :{} Registration Done", groupDTO.getGroupName());
    }

    private void registrationInterface(InterfaceDTO interfaceDTO) {

    }

    private Channel getComponentChannel(List<String> groupHandlerList, List<String> interfacehandlerList) {
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
            ComponentHandler<?> componentHandler = handlerService.getComponentHandler(handler[0]);
            if (Objects.nonNull(componentHandler)) {
                ComponentNode componentNode = ComponentNode.build().setHandler(componentHandler);
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
