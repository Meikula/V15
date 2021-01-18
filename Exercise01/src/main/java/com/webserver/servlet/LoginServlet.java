package com.webserver.servlet;

import http.HttpRequest;
import http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;

public class LoginServlet {
    public void service(HttpRequest request,HttpResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        if(username==null || password==null){
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);
            return;

        }

        try(RandomAccessFile raf = new RandomAccessFile("user.dat","r")){
            System.out.println("2222222222");
            for(int i=0;i<raf.length()/100;i++){
                byte[]data = new byte[32];
                raf.read(data);
                String name = new String(data,"UTF-8").trim();
                if(name.equals(username)){
                    System.out.println("11111111111");
                    raf.read(data);
                    String pw = new String(data,"UTF-8").trim();
                    if(pw.equals(password)){
                        File file = new File("./webapps/myweb/login_success.html");
                        response.setEntity(file);
                        return;
                    }
                    break;
                }

            }
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);

        }catch (Exception e){
            e.printStackTrace();
        }


    }
}
