package pers.kagw.core.protocol.netty;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;
import pers.kagw.core.KagwApplicationContext;

/**
 * @author kwsc98
 */
@Slf4j
public class NettyService {

    EventLoopGroup bossGroup;

    EventLoopGroup workerGroup;


    public NettyService(int port, KagwApplicationContext kagwApplicationContext) {
        //NioEventLoopGroup是对Thread和Selector的封装
        //主线程
        bossGroup = new NioEventLoopGroup(1);
        //工作线程
        workerGroup = new NioEventLoopGroup();
        try {
            //ServerBootstrap作用于Netty引导初始化
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    //绑定服务端通道 NioServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //开启请求缓存队列
                    .option(ChannelOption.SO_BACKLOG, 1024 * 1024)
                    //开启TCP立即响应
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    //开启HTTP长连接支持
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new NettyHttpServerInitializer(kagwApplicationContext));
            Channel channel = b.bind(port).sync().channel();
            log.info("server channel start port:{} channel:{}", port, channel);
        } catch (Exception e) {
            log.error("creat netty service error", e);
        }
    }

    public void close() {
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}
