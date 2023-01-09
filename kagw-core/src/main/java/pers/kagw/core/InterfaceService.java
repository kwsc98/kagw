package pers.kagw.core;

import io.netty.handler.codec.http.FullHttpRequest;
import pers.kagw.core.handler.HandlerService;

/**
 * @author kwsc98
 */
public class InterfaceService {

    private final KagwApplicationContext kagwApplicationContext;



    public InterfaceService(KagwApplicationContext kagwApplicationContext) {
        this.kagwApplicationContext = kagwApplicationContext;
    }

    public String doRun(FullHttpRequest fullHttpRequest) {
        HandlerService handlerService = kagwApplicationContext.getHandlerService();
    }


}
