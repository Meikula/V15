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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){ this.socket=socket; }
    public void run(){
        try {
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);
            String dor = request.getRequestURI();


            HttpServlet servlet = ServerContext.getServlet(dor);

            if(servlet!=null){
                servlet.service(request,response);
            }else{
                File file = new File("./webapps"+dor);
                if(file.exists()&&file.isFile()){
                    response.setEntity(file);

                }else{
                    File notFoundPage = new File("./webapps/root/404.html");
                    response.setEntity(notFoundPage);
                    response.setStatusCode(404);
                    response.setStatusReason("NotFound");
                }

            }

            response.putHeader("Sever","WebServer");
            response.flush();



        }catch(EmptyRequestException e){

        }
        catch (Exception e) {
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
