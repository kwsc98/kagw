package pers.kagw.core.dto;

import com.alibaba.nacos.common.utils.StringUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author kwsc98
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceDTO extends BaseDTO implements Serializable {

    private boolean groupExtends = true;

    private String routeResourceUrl = null;

    public String getRouteResourceUrl() {
        return StringUtils.isNotEmpty(routeResourceUrl) ? routeResourceUrl : super.getResourceUrl();
    }

}
