package com.webserver.servlet;

import http.HttpRequest;
import http.HttpResponse;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class RegServlet extends HttpServlet{
    public void service(HttpRequest request, HttpResponse response){
        String username=request.getParameter("username");
        String password=request.getParameter("password");
        String nickname=request.getParameter("nickname");
        String ageStr=request.getParameter("age");
        System.out.println(username+","+password+","+nickname+","+ageStr);



        if(username==null||password==null||nickname==null||ageStr==null||(!ageStr.matches("[0-9]+"))){
            File file = new File("./webapps/myweb/reg_input_error.html");
            response.setEntity(file);
            return;
        }
        int age = Integer.parseInt(ageStr);
        try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
        ) {
            System.out.println("11111111111");
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name  = new String(data,"UTF-8").trim();
                if(name.equals(username)){
                    File file = new File("./webapps/myweb/have_user.html");
                    response.setEntity(file);
                    return;
                }

            }


            raf.seek(raf.length());
            byte data [] = username.getBytes("UTF-8");
            data= Arrays.copyOf(data,32);
            raf.write(data);

            data = password.getBytes("UTF-8");
            data= Arrays.copyOf(data,32);
            raf.write(data);

            data = nickname.getBytes("UTF-8");
            data= Arrays.copyOf(data,32);
            raf.write(data);

            raf.writeInt(age);

            System.out.println("注册完毕");

            File file = new File("./webapps/myweb/reg_success.html");
            response.setEntity(file);
            System.out.println("12222222");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
