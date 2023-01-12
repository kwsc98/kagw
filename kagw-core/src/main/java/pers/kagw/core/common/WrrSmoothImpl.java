package pers.kagw.core.common;

import pers.kagw.core.dto.RouteNodeDTO;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kwsc98
 */
public class WrrSmoothImpl implements LoadBalancer {

    static class Wrr {
        String addres;
        int weight;
        int current = 0;

        Wrr(RouteNodeDTO routeNodeDTO) {
            this.addres = routeNodeDTO.getAddres();
            this.weight = BigDecimal.valueOf(routeNodeDTO.getWeight() * 100).intValue();
        }
    }

    private Wrr[] cachedWeights;

    @Override
    public void init(List<RouteNodeDTO> list) {
        this.cachedWeights = list.stream().map(Wrr::new).toArray(Wrr[]::new);
    }

    @Override
    public String next() {
        int total = 0;
        Wrr shed = cachedWeights[0];
        for (Wrr item : cachedWeights) {
            int weight = item.weight;
            total += weight;
            item.current += weight;
            if (item.current > shed.current) {
                shed = item;
            }
        }
        shed.current -= total;
        return shed.addres;
    }
}
