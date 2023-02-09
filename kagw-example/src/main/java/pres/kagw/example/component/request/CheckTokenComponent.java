package pres.kagw.example.component.request;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pers.kagw.core.dto.RequestHandlerDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.handler.BaseComponentHandler;
import pers.kagw.core.handler.RequestComponentHandler;
import pres.kagw.example.common.dto.CommonRequest;
import pres.kagw.example.common.enums.ErrorEnum;
import pres.kagw.example.service.TokenService;

import javax.annotation.Resource;
import java.awt.*;

/**
 * @author kwsc98
 */
@Component("CheckTokenComponent")
@Slf4j
public class CheckTokenComponent extends RequestComponentHandler<RequestHandlerDTO<CommonRequest>, Object> {

    @Resource
    private TokenService tokenService;

    @Override
    public Object handle(RequestHandlerDTO<CommonRequest> requestHandlerDTO, Object config) {
        log.debug("CheckTokenComponent Handler Start");
        CommonRequest commonRequest = requestHandlerDTO.getContent();
        String key = commonRequest.getUserId() + ":" + commonRequest.getMerchantNo();
        String token = tokenService.getToken(key);
        if (StringUtils.isEmpty(token) || !token.equals(commonRequest.getToken())) {
            log.debug("Token Not Exist");
            throw new ApiGateWayException(ErrorEnum.TOKEN_ERROR);
        }
        log.debug("CheckTokenComponent Handler Start");
        return requestHandlerDTO;
    }

}
