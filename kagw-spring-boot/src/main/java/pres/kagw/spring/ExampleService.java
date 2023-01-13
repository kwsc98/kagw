package pres.kagw.spring;


import pers.kagw.core.KagwApplicationContext;
import pers.kagw.core.dto.GroupDTO;
import pers.kagw.core.dto.RouteNodeDTO;

import java.util.ArrayList;
import java.util.List;

public class ExampleService {


    public static void main(String[] args) {
        KagwApplicationContext kagwApplicationContext = new KagwApplicationContext(null, 8080);
        GroupDTO groupDTO = new GroupDTO();
        //设置超时间
        groupDTO.setTimeOut(1000);
        groupDTO.setResourceName("merchant");
        groupDTO.setResourceUrl("/merchant");
        //开通组件配置
        List<RouteNodeDTO> routeNodeDTOS = new ArrayList<>();
        RouteNodeDTO routeNodeDTO1 = new RouteNodeDTO().setWeight(1).setAddres("127.0.0.1:8080");
        RouteNodeDTO routeNodeDTO2 = new RouteNodeDTO().setWeight(1).setAddres("127.0.0.1:8081");
        routeNodeDTOS.add(routeNodeDTO1);
        routeNodeDTOS.add(routeNodeDTO2);
        groupDTO.setRouteList(routeNodeDTOS);
        kagwApplicationContext.getChannelService().registrationGroup(groupDTO);
    }
}
