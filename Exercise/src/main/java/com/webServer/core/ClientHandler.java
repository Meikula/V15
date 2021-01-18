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

public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket){ this.socket=socket; }
    public void run(){
        try {
            HttpRequest request = new HttpRequest(socket);
            HttpResponse response = new HttpResponse(socket);

            String dor = request.getRequestURI();

            if("/myweb/regUser".equals(dor)){
                RegServlet regServlet = new RegServlet();
                regServlet.service(request,response);

            }else if("/myweb/loginUser".equals(dor)){
                LoginServlet loginServlet = new LoginServlet();
                loginServlet.service(request,response);

            }else if("/myweb/showAllUser".equals(dor)){
                ShowAllUserServlet allUserServlet = new ShowAllUserServlet();
                allUserServlet.service(request,response);

            }

            else{
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
