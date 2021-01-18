package com.webServer.core;

import http.EmptyRequestException;
import http.HttpContent;
import http.HttpRequest;
import http.HttpResponse;
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
            if("/myweb/regUser".equals(path)){
                RegServlet servlet = new RegServlet();
                servlet.service(request,response);

            }else if("/myweb/showAllUser".equals(path)){
                ShowAllUserServlet showAllUserServlet = new ShowAllUserServlet();
                showAllUserServlet.service(request,response);

            }

            else if("/myweb/loginUser".equals(path)) {

                LoginServlet servlet = new LoginServlet();
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
