package pers.kagw.core.dto;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import pers.kagw.core.common.LoadBalancer;
import pers.kagw.core.common.WrrSmoothImpl;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author kwsc98
 */
@Data
@Slf4j
public class GroupDTO implements Serializable {

    private String groupName;

    private String preUrl;

    private List<RouteNodeDTO> routeList;

    private List<String> handlerList;

    private List<InterfaceDTO> interfaceDTOList;

    private LoadBalancer loadBalancer;

    public void init() {
        if (Objects.isNull(routeList) || routeList.isEmpty()) {
            log.error("GroupDTO: {} Init Error: RouteList Is Empty", groupName);
        }
        WrrSmoothImpl wrrSmooth = new WrrSmoothImpl();
        wrrSmooth.init(routeList);
        this.loadBalancer = wrrSmooth;
    }


}
