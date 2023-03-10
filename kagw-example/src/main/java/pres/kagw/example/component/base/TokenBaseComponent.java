package pres.kagw.example.component.base;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pers.kagw.core.common.JsonUtils;
import pers.kagw.core.dto.RequestHandlerDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.handler.BaseComponentHandler;
import pres.kagw.example.common.CommonCode;
import pres.kagw.example.common.dto.CommonRequest;
import pres.kagw.example.common.dto.CommonResponse;
import pres.kagw.example.common.enums.ErrorEnum;
import pres.kagw.example.service.TokenService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author kwsc98
 */
@Component("TokenBaseComponent")
@Slf4j
public class TokenBaseComponent extends BaseComponentHandler<RequestHandlerDTO<CommonRequest>, Long> {

    @Resource
    private TokenService tokenService;

    @Override
    public Object handle(RequestHandlerDTO<CommonRequest> requestHandlerDTO, Long timeOut) {
        log.debug("TokenBaseComponent Handler Start");
        try {
            CommonRequest commonRequest = requestHandlerDTO.getContent();
            String key = commonRequest.getUserId() + ":" + commonRequest.getMerchantNo();
            String token = tokenService.setToken(key, timeOut);
            log.debug("TokenBaseComponent Handler Start");
            return token;
        } catch (Exception e) {
            log.error("Get Token Error : {}]", e.toString());
            throw new ApiGateWayException(ErrorEnum.TOKEN_ERROR);
        }
    }

    public Long init(String configStr) {
        if (StringUtils.isNotEmpty(configStr)) {
            return Long.valueOf(configStr);
        }
        return 60L;
    }

}
