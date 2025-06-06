package com.itmo.nxzage.client;

public final class Client {

    private Client() {
        throw new UnsupportedOperationException("This is an utility class and can not be instantiated");
    }

    public static void main(String[] args) {
        App app = new App();
        // TODO IMPORTANT make commandlinearg
        String serverFilename = args.length > 0 ? args[0] : "localdir/store_file.csv";
        System.out.println(System.getProperty("user.dir"));
        var server = new com.itmo.nxzage.server.App();
        server.init(serverFilename);
        app.init(server);
        app.run();
    }
}
