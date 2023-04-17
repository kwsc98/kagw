package pres.kagw.example.component.base;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import pers.kagw.core.dto.RequestHandlerDTO;
import pers.kagw.core.handler.BaseComponentHandler;
import pres.kagw.example.common.dto.CommonRequest;
import pres.krpc.exampe.ExampeService;
import pres.krpc.exampe.dto.RequestDTO;
import pres.krpc.spring.annotation.KrpcResource;

import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author kwsc98
 */
@Component("KrpcBaseComponent")
@Slf4j
public class KrpcBaseComponent extends BaseComponentHandler<RequestHandlerDTO<CommonRequest>, Object> {

    @KrpcResource(version = "1.0.1",timeout = 1000)
    private ExampeService exampeService;

    @Override
    public Object handle(RequestHandlerDTO<CommonRequest> object, Object config) {
        RequestDTO requestDTO = new RequestDTO();
        requestDTO.setDate(new Date());
        Date date = exampeService.doRun(requestDTO).getDate();
        SimpleDateFormat myFmt = new SimpleDateFormat("yyyy年MM月dd日 HH时mm分ss秒");
        return myFmt.format(date);
    }
}
