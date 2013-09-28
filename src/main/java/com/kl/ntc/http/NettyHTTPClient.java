package com.kl.ntc.http;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;

public class NettyHTTPClient extends Thread {
    private static final Logger LOG = Logger.getLogger(NettyHTTPClient.class.getName());

    private final String host;
    private final int port;
    private final String path;

    public NettyHTTPClient(String host, int port, String path) {
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public void run() {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new LoggingHandler(LogLevel.INFO),
                            new HttpClientCodec(),
                            new NettyHTTPClientHandler());
                }
            });

            // Start the connection attempt.
            Channel ch = b.connect(host, port).sync().channel();

            int numberOfOperations = 2;
            for (int i = 0; i < numberOfOperations; i++) {
                // Send the login message
                long rnd = Math.round(Math.random() * 100);
                String msg = "{\"userName\":\"testUser\",\"password\":\"testPassword" + rnd + "\"}";

                // Prepare the HTTP request.
                FullHttpRequest request = new DefaultFullHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.POST, path, Unpooled.copiedBuffer(msg, CharsetUtil.UTF_8));
                request.headers().set(HttpHeaders.Names.HOST, host);
                request.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.CLOSE);
                request.headers().set(HttpHeaders.Names.ACCEPT_ENCODING, HttpHeaders.Values.GZIP);
                request.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain; charset=UTF-8");
                request.headers().set(HttpHeaders.Names.CONTENT_LENGTH, request.content().readableBytes());

                ch.writeAndFlush(request);
            }

            // Wait for the server to close the connection.
            ch.closeFuture().sync();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            group.shutdownGracefully();
        }
    }

}
