package com.kl.ntc.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

public class NettyHTTPClientHandler extends SimpleChannelInboundHandler<HttpObject> {
    private static final Logger LOG = Logger.getLogger(NettyHTTPClientHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {
        if (msg instanceof HttpResponse) {
            HttpResponse response = (HttpResponse) msg;
            LOG.info("Status: " + response.getStatus());
            LOG.info("Protocol: " + response.getProtocolVersion());
            LOG.info("<JSON>");
        }
        if (msg instanceof HttpContent) {
            HttpContent content = (HttpContent) msg;
            LOG.info(content.content().toString(CharsetUtil.UTF_8));
            if (content instanceof LastHttpContent) {
                LOG.info("</JSON>");
            }
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        LOG.warn("Unexpected exception from channel. " + cause.getMessage());
        ctx.close();
    }
}
