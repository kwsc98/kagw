package pers.kagw.core.dto;

import lombok.Data;
import pers.kagw.core.channel.Channel;
import pers.kagw.core.common.LoadBalancer;

import java.util.List;

/**
 * @author kwsc98
 */
@Data
public class ResourceDTO {
    private Channel channel;

    private BaseDTO baseDTO;

    private String routeResourceUrl;

    private LoadBalancer loadBalancer;

    private List<String> groupHandlerList;

    private List<String> handlerList;

    public static ResourceDTO build(){
        return new ResourceDTO();
    }

    public ResourceDTO setChannel(Channel channel) {
        this.channel = channel;
        return this;
    }

    public ResourceDTO setBaseDTO(BaseDTO baseDTO) {
        this.baseDTO = baseDTO;
        return this;
    }

    public ResourceDTO setLoadBalancer(LoadBalancer loadBalancer) {
        this.loadBalancer = loadBalancer;
        return this;
    }

    public ResourceDTO setRouteResourceUrl(String routeResourceUrl) {
        this.routeResourceUrl = routeResourceUrl;
        return this;
    }

    public ResourceDTO setGroupHandlerList(List<String> groupHandlerList) {
        this.groupHandlerList = groupHandlerList;
        return this;
    }

    public ResourceDTO setHandlerList(List<String> handlerList) {
        this.handlerList = handlerList;
        return this;
    }
}
