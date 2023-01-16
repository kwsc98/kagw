package pers.kagw.core.handler.impl;

import com.alibaba.nacos.common.utils.StringUtils;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.DefaultEventLoop;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.DefaultPromise;
import io.netty.util.concurrent.Promise;
import lombok.extern.slf4j.Slf4j;
import pers.kagw.core.KagwApplicationContext;
import pers.kagw.core.common.LoadBalancer;
import pers.kagw.core.dto.ResourceDTO;
import pers.kagw.core.exception.ApiGateWayException;
import pers.kagw.core.handler.RequestComponentHandler;
import pers.kagw.core.protocol.netty.NettyClient;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @author kwsc98
 */
@Slf4j
public class NettyClientComponentHandler extends RequestComponentHandler<FullHttpRequest, ResourceDTO> {

    private final NettyClient nettyClient;

    public NettyClientComponentHandler() {
        this.nettyClient = new NettyClient();
    }

    @Override
    public Object handle(FullHttpRequest fullHttpRequest, ResourceDTO resourceDTO) {
        log.debug("NettyClientComponentHandler Start Handler");
        try {
            String[] urlString = resourceDTO.getLoadBalancer().next().split(":");
            String ip = urlString[0];
            if (StringUtils.isNotEmpty(resourceDTO.getRouteResourceUrl())) {
                fullHttpRequest.setUri(resourceDTO.getRouteResourceUrl());
            }
            int port = Integer.parseInt(urlString[1]);
            Channel channel = nettyClient.getChannel(ip, port);
            Promise<Object> promise = new DefaultPromise<>(new DefaultEventLoop());
            String uuid = java.util.UUID.randomUUID().toString();
            NettyClient.MSG_CACHE.put(uuid, promise);
            channel.writeAndFlush(fullHttpRequest);
            Object object = promise.get(resourceDTO.getBaseDTO().getTimeOut(), TimeUnit.MILLISECONDS);
            log.debug("NettyClientComponentHandler Start Done");
            return object;
        } catch (Exception e) {
            log.error("NettyClientComponentHandler Error", e);
            throw new ApiGateWayException();
        }

    }

}
