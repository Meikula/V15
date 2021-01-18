package com.webServer.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private ServerSocket serverSocket;

    public WebServer() {

        try {
            System.out.println("正在启动服务端...");
            serverSocket = new ServerSocket(8088);
            System.out.println("服务端启动完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    public void start(){

        try {
            while (true) {
                System.out.println("正在连接客户端");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端已连接!");
                ClientHandler handler = new ClientHandler(socket);
                Thread t = new Thread(handler);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer w = new WebServer();
        w.start();

    }
}
