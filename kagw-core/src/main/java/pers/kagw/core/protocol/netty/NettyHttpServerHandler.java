package pers.kagw.core.protocol.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;
import pers.kagw.core.KagwApplicationContext;
import pers.kagw.core.common.JsonUtils;

/**
 * @author kwsc98
 */
@Slf4j
public class NettyHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

    private final KagwApplicationContext kagwApplicationContext;

    public NettyHttpServerHandler(KagwApplicationContext kagwApplicationContext) {
        this.kagwApplicationContext = kagwApplicationContext;
    }


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            //获取httpRequest
            FullHttpRequest httpRequest = (FullHttpRequest) msg;
            try {
                //获取请求路径、请求体、请求方法
                String uri = httpRequest.uri();
                String content = httpRequest.content().toString(CharsetUtil.UTF_8);
                HttpMethod method = httpRequest.method();
                log.info("Request Url: {} ,Content: {} ,Method: {} ", uri, JsonUtils.formatJsonStr(content), method);
                //网关处理逻辑
                String responseMsg = this.kagwApplicationContext.getInterfaceService().doRun(httpRequest);
                log.info("Response Url: {} ,Content: {} ,Method: {} ", uri, responseMsg, method);
                FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.copiedBuffer(responseMsg, CharsetUtil.UTF_8));
                response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain;charset=UTF-8");
                ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
            } catch (Exception e) {
                log.error("Netty Handler Error ", e);
            } finally {
                httpRequest.release();
            }
        }
    }
}
