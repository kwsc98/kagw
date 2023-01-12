package pers.kagw.core.common;

import pers.kagw.core.dto.RouteNodeDTO;

import java.util.List;

public interface LoadBalancer {

    void init(List<RouteNodeDTO> list);

    String next();

}
