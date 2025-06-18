package com.itmo.nxzage.client.net;

public class ClientConfig {
    private static String hostname = "192.168.10.80"; // se.ifmo.ru
    // private static String hostname = "localhost";
    private static int hostport = 3777;

    public ClientConfig() {
        throw new IllegalStateException("Util class cannot be initialized");
    }

    public static String getHostname() {
        return hostname;
    }

    public static void setHostname(String hostname) {
        ClientConfig.hostname = hostname;
    }

    public static int getHostport() {
        return hostport;
    }

    public static void setHostport(int port) {
        ClientConfig.hostport = port;
    }

}
