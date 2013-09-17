package com.kl.nts;

import com.kl.nts.server.NettyTCPServer;

public class LaunchPad {

    public static void main(String[] args) throws Exception {
        int port = 8000;
        new NettyTCPServer(port).start();
    }
}
