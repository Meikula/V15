package com.webserver.core;

import http.EmptyRequestException;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WebServer {
    private ServerSocket serverSocket;
    private ExecutorService threadPool;
    public WebServer(){

        try {
            System.out.println("启动服务端...");
            serverSocket = new ServerSocket(8088);
            threadPool= Executors.newFixedThreadPool(50);
            System.out.println("启动服务端启动完毕!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void start(){

        try {
            while (true) {
                System.out.println("正在连接客户端...");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端已连接!");
                ClientHandler clientHandler = new ClientHandler(socket);
                threadPool.execute(clientHandler);
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
