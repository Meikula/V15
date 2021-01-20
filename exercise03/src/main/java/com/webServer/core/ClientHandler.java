package com.webServer.core;

import http.EmptyRequestException;
import http.HttpContent;
import http.HttpRequest;
import http.HttpResponse;
import servlet.HttpServlet;
import servlet.LoginServlet;
import servlet.RegServlet;
import servlet.ShowAllUserServlet;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable {
    private Socket socket;
    public ClientHandler(Socket socket) {
        this.socket = socket;
    }
    public void run(){
        try {

            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);
            String path = request.getRequestURI();
            HttpServlet servlet = ServerContext.getMapping(path);
            System.out.println(path);
            System.out.println(servlet);
            if(servlet!=null){
                servlet.service(request,response);
            }else{
                File file = new File("./webapps"+path);
                if(file.exists()&&file.isFile()) {
                    response.setsFile(file);
                }else{
                    File file1 = new File("./webapps/root/404.html");
                    response.setsFile(file1);
                    response.setName("NotFound");
                    response.setNum(404);
                }
            }
            response.putHeader("Sever","WebServer");
            response.flush();
        } catch(EmptyRequestException e){
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
