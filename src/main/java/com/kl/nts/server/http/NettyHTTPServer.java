package com.kl.nts.server.http;

import com.kl.nts.server.JsonDecoder;
import com.kl.nts.server.JsonEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.log4j.Logger;

public class NettyHTTPServer extends Thread {
    private static final Logger LOG = Logger.getLogger(NettyHTTPServer.class.getName());

    private final int port;

    public NettyHTTPServer(int port) {
        this.port = port;
    }

    public void run() {
        EventLoopGroup bossGroup = new NioEventLoopGroup(1 /* number of threads */ );
        EventLoopGroup workerGroup = new NioEventLoopGroup(2 /* number of threads */);

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new LoggingHandler(LogLevel.INFO),
                            new HttpRequestDecoder(),
                            new HttpObjectAggregator(Integer.MAX_VALUE),
                            new HttpDecoder(),
                            new JsonDecoder(),
                            new HttpResponseEncoder(),
                            new HttpEncoder(),
                            new JsonEncoder(),
                            new NettyHTTPServerHandler());
                }
            });
            b.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);

            // Bind and start to accept incoming connections.
            Channel ch = b.bind(port).sync().channel();
            LOG.info("HTTP Server started on port [" + port + "]");

            ch.closeFuture().sync();
        } catch (Exception e){
            LOG.error(e.getMessage(), e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
