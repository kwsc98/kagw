package pers.kagw.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;

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


    public String getResourcesRouteResourceUrl() {
        return StringUtils.isNotEmpty(super.getRouteResourceUrl()) ? super.getRouteResourceUrl() : super.getResourceUrl();
    }

}
