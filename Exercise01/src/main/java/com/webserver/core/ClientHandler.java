package com.webserver.core;


import com.webserver.servlet.HttpServlet;
import com.webserver.servlet.LoginServlet;
import com.webserver.servlet.RegServlet;
import com.webserver.servlet.ShowAllUserServlet;
import http.EmptyRequestException;
import http.HttpContext;
import http.HttpRequest;
import http.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){
        this.socket=socket;
    }
    public void run(){
        try {
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);


            String path = request.getRequestURI();
            Map<String, HttpServlet> servletMapping=new HashMap<>();
            servletMapping.put("/myweb/regUser",new RegServlet());
            servletMapping.put("/myweb/loginUser",new LoginServlet());
            servletMapping.put("/myweb/showAllUser",new ShowAllUserServlet());


            /*if("/myweb/regUser".equals(path)) {
                RegServlet regServlet = new RegServlet();
                regServlet.service(request, response);
            }*/
            HttpServlet servlet = servletMapping.get(path);
            if(servlet!=null){
                servlet.service(request,response);
            }else{
                File file = new File("./webapps"+path);
                if(file.exists()&&file.isFile()){
                    response.setEntity(file);
                }else{
                    File f = new File("./webapps/root/404.html");
                    response.setEntity(f);
                    response.setStatusCode(404);
                    response.setStatusReason("NotFound");
                }



            }
            response.flush();
            System.out.println("响应发送完毕!");


        }catch(EmptyRequestException e){
        }catch (Exception e) {
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
