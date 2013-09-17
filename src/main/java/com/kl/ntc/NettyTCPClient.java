package com.kl.ntc;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class NettyTCPClient {
    private final String host;
    private final int port;

    public NettyTCPClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void run() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(
                            new LoggingHandler(LogLevel.INFO),
                            new StringEncoder(),
                            new StringDecoder(),
                            new NettyTCPClientHandler());
                }
            });

            // Start the connection attempt.
            Channel ch = b.connect(host, port).sync().channel();

            // Send the login message
            long rnd = Math.round(Math.random() * 100);
            String msg = "{\"userName\":\"testUser\",\"password\":\"testPassword" + rnd + "\"}";
            ch.writeAndFlush(msg);

            // Wait for the server to close the connection.
            ch.closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }

}
