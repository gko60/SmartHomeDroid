package com.homematic.bidcos.rpc;

public interface RpcServer {

    String getUrl();

    int getPort();

    void start();

    void stop();
}
