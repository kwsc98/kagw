package pres.kagw.example.component.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pers.kagw.core.dto.RequestHandlerDTO;
import pers.kagw.core.handler.BaseComponentHandler;

/**
 * @author kwsc98
 */
@Component("MockComponent")
@Slf4j
public class MockBaseComponent extends BaseComponentHandler<RequestHandlerDTO<Object>, Object> {

    @Override
    public Object handle(RequestHandlerDTO<Object> object, Object config) {
        log.debug("Mock Info : {}", config);
        return config;
    }

    public Object init(String configStr) {
        return configStr;
    }

}
