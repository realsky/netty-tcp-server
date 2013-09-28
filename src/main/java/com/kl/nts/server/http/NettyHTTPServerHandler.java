package com.kl.nts.server.http;

import com.kl.nts.json.AuthMessage;
import com.kl.nts.task.ClientTask;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

public class NettyHTTPServerHandler extends SimpleChannelInboundHandler<Object> {
    private static final Logger LOG = Logger.getLogger(NettyHTTPServerHandler.class.getName());

    private ClientTask task;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        LOG.info(msg.toString());
        if (msg instanceof AuthMessage) {
            if(ctx.channel().isOpen()) {
                task.acceptRequest((AuthMessage) msg);
            }
        } else if (msg instanceof String) {
            LOG.info("Message is string: [" + msg + "].");
            ctx.writeAndFlush(((String) msg).toUpperCase());
        } else {
            LOG.warn("Message is not valid.");
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        task = new ClientTask(ctx.channel());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        task.unregisteredChannel();
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
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
