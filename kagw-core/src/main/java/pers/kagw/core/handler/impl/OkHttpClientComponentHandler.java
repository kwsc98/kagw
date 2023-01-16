package pers.kagw.core.handler.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pers.kagw.core.dto.ResourceDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.handler.RequestComponentHandler;
import pers.kagw.core.protocol.okhttp.OkHttpClientService;

import java.util.Objects;

/**
 * @author kwsc98
 */
@Slf4j
public class OkHttpClientComponentHandler extends RequestComponentHandler<FullHttpRequest, ResourceDTO> {


    private final OkHttpClientService okHttpClientService;

    public OkHttpClientComponentHandler() {
        this.okHttpClientService = new OkHttpClientService();
    }


    @Override
    public Object handle(FullHttpRequest fullHttpRequest, ResourceDTO resourceDTO) {
        log.debug("OkHttpClientComponentHandler Start Handler");
        Response response = null;
        try {
            String addres = resourceDTO.getLoadBalancer().next();
            if (StringUtils.isNotEmpty(resourceDTO.getRouteResourceUrl())) {
                fullHttpRequest.setUri(resourceDTO.getRouteResourceUrl());
            }
            RequestBody body = RequestBody.create(fullHttpRequest.content().toString(CharsetUtil.UTF_8), MediaType.get("application/json; charset=utf-8"));
            String resourceUrl = fullHttpRequest.uri();
            if (StringUtils.isNotEmpty(resourceDTO.getRouteResourceUrl())) {
                resourceUrl = resourceDTO.getRouteResourceUrl();
            }
            String url = addres + resourceUrl;
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            response = okHttpClientService.execute(request);
            String responseStr = null;
            if (Objects.nonNull(response.body())) {
                responseStr = response.body().string();
            }
            log.debug("OkHttpClientComponentHandler Start Done");
            return responseStr;
        } catch (Exception e) {
            log.error("OkHttpClientComponentHandler Error :{}", e.toString());
            throw new ApiGateWayException();
        } finally {
            if (Objects.nonNull(response)) {
                response.close();
            }
        }
    }
}
