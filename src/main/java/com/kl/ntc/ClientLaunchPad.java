package com.kl.ntc;

import com.kl.ntc.http.NettyHTTPClient;
import com.kl.ntc.tcp.NettyTCPClient;

public class ClientLaunchPad {

    public static void main(String[] args) throws Exception {
        final String host = "localhost";
        final String path = "/";
        final int tcpPort = 8000;
        final int httpPort = 8080;

//        NettyTCPClient tcpClient = new NettyTCPClient(host, tcpPort);
//        tcpClient.start();

        NettyHTTPClient httpClient = new NettyHTTPClient(host, httpPort, path);
        httpClient.start();

//        tcpClient.join();
        httpClient.join();
    }
}
