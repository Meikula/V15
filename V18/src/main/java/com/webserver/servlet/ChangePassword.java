package com.webserver.servlet;

import com.webserver.http.HttpRequest;
import com.webserver.http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;

public class ChangePassword extends HttpServlet{
    public  void service(HttpRequest request, HttpResponse response){

        String username = request.getParameter("username");
        String oldPassword=request.getParameter("oldPassword");
        String newPassword=request.getParameter("newPassword");
        if (username==null || oldPassword == null || newPassword==null || newPassword.equals(oldPassword) ) {
            File file = new File("./webapps/myweb/password_fail.html");
            response.setEntity(file);
            return;
        }

        try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw")){
            for(int i=0;i<raf.length()/100;i++) {
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data, "UTF-8").trim();
                //String username = request.getParameter("username");
                System.out.println(username+","+name);
                if(username.equals(name)) {
                    data = new byte[32];
                    raf.read(data);
                    String opd = new String(data,"UTF-8").trim();
                    //String oldPassword=request.getParameter("oldPassword");
                    System.out.println(oldPassword+","+opd);
                    if(opd.equals(oldPassword)){
                        raf.seek(i*100+32);
                        //String newPassword=request.getParameter("newPassword");
                        System.out.println("new:"+newPassword);
                        data = newPassword.getBytes("UTF-8");

                        raf.write(data);
                        System.out.println("修改完毕");
                        File file = new File("./webapps/myweb/password_success.html");
                        response.setEntity(file);
                        return;
                    }
                    break;
                }
            }
            File file = new File("./webapps/myweb/password_fail.html");
            response.setEntity(file);

        }catch(Exception e){
            e.printStackTrace();
        }



        }

    }

