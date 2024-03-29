package pers.kagw.core.protocol.netty;


import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import pers.kagw.core.KagwApplicationContext;
import pers.kagw.core.common.JsonUtils;
import pers.kagw.core.dto.RequestHandlerDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.exception.ExceptionEnum;

import java.util.Objects;
import java.util.UUID;

/**
 * @author kwsc98
 */
@Slf4j
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final KagwApplicationContext kagwApplicationContext;

    public NettyHttpServerHandler(KagwApplicationContext kagwApplicationContext) {
        this.kagwApplicationContext = kagwApplicationContext;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        log.info("Server Channel Connection: [{}] parent [{}]", ctx.channel(), ctx.channel().parent());
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest httpRequest) throws Exception {
        MDC.put("UUID", UUID.randomUUID().toString());
        Object responseObject = null;
        try {
            //获取请求路径、请求体、请求方法
            String url = httpRequest.uri();
            String content = httpRequest.content().toString(CharsetUtil.UTF_8);
            HttpMethod method = httpRequest.method();
            RequestHandlerDTO<Object> requestHandlerDTO = RequestHandlerDTO.build().setContent(content).setResourceUrl(url).setHttpMethod(method);
            log.info("Request Url : {} ,Content : {} ,Method : {} ", url, content, method);
            //网关处理逻辑
            try {
                responseObject = this.kagwApplicationContext.getDisposeService().dealWith(requestHandlerDTO);
            } catch (ApiGateWayException apiGateWayException) {
                String infoStr = apiGateWayException.getInfoStr();
                log.debug("DealWith Error : {}", infoStr);
                if (StringUtils.isNotEmpty(infoStr)) {
                    responseObject = infoStr;
                }
            }
            if (Objects.isNull(responseObject) || (!(responseObject instanceof String))) {
                responseObject = JsonUtils.writeValueAsString(responseObject);
            }
            log.info("Response Url : {} ,Content: {} ,Method : {} ", url, responseObject, method);
        } catch (Exception e) {
            log.error("Netty Handler Error : {}", e.toString(), e);
        } finally {
            MDC.remove("UUID");
            if (Objects.isNull(responseObject)) {
                responseObject = ExceptionEnum.ERROR.getExceptionInfoStr();
            }
            FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(responseObject.toString(), CharsetUtil.UTF_8));
            response.headers().set(HttpHeaderNames.CONTENT_TYPE, HttpHeaderValues.APPLICATION_JSON);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Server Channel Error : {}", cause.toString());
        ctx.close();
    }
}
