package pres.kagw.example.component.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pers.kagw.core.handler.ResponseComponentHandler;
import pres.kagw.example.common.dto.CommonResponse;
import pres.kagw.example.common.enums.ResponseEnum;

/**
 * @author kwsc98
 */
@Component("CommonResponseComponent")
@Slf4j
public class CommonResponseComponent extends ResponseComponentHandler<Object, Object> {

    @Override
    public Object handle(Object object, Object config) {
        log.debug("CommonResponseComponent Handler Start");
        CommonResponse commonResponse = CommonResponse.build(ResponseEnum.SUCCESS).setResData(object);
        log.debug("CommonResponseComponent Handler Done");
        return commonResponse;
    }

}
