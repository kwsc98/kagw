package pers.kagw.core.dto;

import lombok.Data;

import java.util.List;

/**
 * @author kwsc98
 */
@Data
public class InterfaceDTO {

    private String interfaceName;

    private String url;

    private List<InterfaceDTO> interfaceDTOList;

}
