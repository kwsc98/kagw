package pers.kagw.core.dto;

import lombok.Data;

import java.util.List;

/**
 * @author kwsc98
 */
@Data
public class BaseDTO {

    private String resourceName;

    private String resourceUrl;

    private long timeOut = 3000;

    private List<String> handlerList;

}
