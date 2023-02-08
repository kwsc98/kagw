package pres.kagw.example.component.base;

import com.fasterxml.jackson.databind.util.JSONPObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import pers.kagw.core.common.JsonUtils;
import pers.kagw.core.dto.RequestHandlerDTO;
import pers.kagw.core.handler.BaseComponentHandler;
import pres.kagw.example.common.CommonCode;
import pres.kagw.example.common.dto.CommonRequest;
import pres.kagw.example.common.dto.CommonResponse;
import pres.kagw.example.service.TokenService;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author kwsc98
 */
@Component("TokenBaseComponent")
@Slf4j
public class TokenBaseComponent extends BaseComponentHandler<RequestHandlerDTO<Object>, Long> {

    @Resource
    private TokenService tokenService;

    @Override
    public Object handle(RequestHandlerDTO<Object> requestHandlerDTO, Long timeOut) {
        try {
            CommonRequest commonRequest = JsonUtils.readValue((String) requestHandlerDTO.getContent(), CommonRequest.class);
            String key = commonRequest.getUserId() + ":" + commonRequest.getMerchantNo();
            String token = tokenService.setToken(key, timeOut);
            return CommonResponse.build().setCode(CommonCode.SUCCESS).setInfoStr(token);
        } catch (Exception e) {
            log.error("Get Token Error : {}]", e.toString());
            return CommonResponse.build().setCode(CommonCode.ERROR);
        }
    }

    public Long init(String configStr) {
        if (StringUtils.isNotEmpty(configStr)) {
            return Long.valueOf(configStr);
        }
        return 60L;
    }

}
