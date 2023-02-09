package pres.kagw.example.component.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pers.kagw.core.common.JsonUtils;
import pers.kagw.core.dto.RequestHandlerDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.handler.RequestComponentHandler;
import pres.kagw.example.common.dto.CommonRequest;
import pres.kagw.example.common.enums.ErrorEnum;

/**
 * @author kwsc98
 */
@Component("CommonRequestComponent")
@Slf4j
public class CommonRequestComponent extends RequestComponentHandler<RequestHandlerDTO<Object>, Object> {
    @Override
    public Object handle(RequestHandlerDTO<Object> object, Object config) {
        try {
            log.debug("CommonRequestComponent Handler Start");
            Object bodContent = object.getContent();
            if (bodContent instanceof String) {
                object.setContent(JsonUtils.readValue((String) bodContent, CommonRequest.class));
            }
            log.debug("CommonRequestComponent Handler Done");
            return object;
        } catch (Exception e) {
            log.error("CommonRequestComponent Error");
            throw new ApiGateWayException(ErrorEnum.REQUEST_ERROR);
        }
    }
}
