package com.kl.ntc;

public class ClientLaunchPad {

    public static void main(String[] args) throws Exception {
        final String host = "localhost";
        final int port = 8000;
        new NettyTCPClient(host, port).run();
    }
}
