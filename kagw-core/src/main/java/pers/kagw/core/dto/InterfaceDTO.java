package pers.kagw.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author kwsc98
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class InterfaceDTO extends BaseDTO implements Serializable {

    private boolean groupExtends = true;

    private List<InterfaceDTO> interfaceDTOList;

}
