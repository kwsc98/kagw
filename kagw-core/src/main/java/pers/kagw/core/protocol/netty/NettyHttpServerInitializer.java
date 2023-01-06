package pers.kagw.core.protocol.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.HttpServerExpectContinueHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author kwsc98
 */
@Slf4j
public class NettyHttpServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline p = ch.pipeline();
        // 解码为HttpRequest
        p.addLast(new HttpServerCodec());
        // 解码成FullHttpRequest
        p.addLast(new HttpObjectAggregator(1024 * 1024));
        // 处理POST请求中 100-continue 状态数据
        p.addLast(new HttpServerExpectContinueHandler());
        // Http请求处理
        p.addLast(new NettyHttpServerHandler());
    }
}
