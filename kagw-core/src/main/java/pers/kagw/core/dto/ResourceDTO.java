package pers.kagw.core.dto;

import lombok.Data;
import pers.kagw.core.channel.Channel;
import pers.kagw.core.common.LoadBalancer;

/**
 * @author kwsc98
 */
@Data
public class ResourceDTO {
    private Channel channel;

    private BaseDTO baseDTO;

    private String routeResourceUrl;

    private LoadBalancer loadBalancer;

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
}
