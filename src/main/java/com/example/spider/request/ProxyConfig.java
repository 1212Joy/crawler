package com.example.spider.request;

import lombok.Data;

/**
 * Created by zhuzhihang on 2016/12/16.
 */
@Data
public class ProxyConfig {
    private String host;
    private int port;
    private String user;
    private String password;

    public ProxyConfig(String host, int port, String user, String password) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
    }

    public static ProxyConfig build() {
        return new ProxyConfig("proxy.abuyun.com", 9010, "H29963RXO132Y95D", "554205C0E1903976");
    }
}
