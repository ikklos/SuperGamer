package ikklo.server;


import java.net.InetAddress;

public class GamerServer {
    public static void main(String[] args) throws Exception {
        Server server = new Server(InetAddress.getLocalHost(),9999);
        server.runServer("jdbc:mysql://127.0.0.1:3306/supergamerdb?characterEncoding=UTF-8","root","693147xx");

    }
}