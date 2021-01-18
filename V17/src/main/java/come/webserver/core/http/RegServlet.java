package come.webserver.core.http;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Arrays;

public class RegServlet {
    public void service(HttpRequest request,HttpResponse response) {
        System.out.println("RegServlet:开始处理注册....");
        //1获取用户在注册页面上输入的注册信息
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        String nickname = request.getParameters("nickname");
        String ageStr = request.getParameters("age");
        System.out.println(username + "," + password + "," + nickname + "," + ageStr);

        if(username==null||password==null||nickname==null||ageStr==null||!(ageStr.matches("[0-9]+"))){
            File file = new File("./webapps/myweb/reg_input_error.html");
            response.setEntity(file);
            return;
        }

        int age = Integer.parseInt(ageStr);

        try(
                RandomAccessFile raf = new RandomAccessFile("user.dat","rw");
        ) {
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name= new String(data, "UTF-8").trim();
                if(name.equals(username)){
                    File file = new File("./webapps/myweb/have_user.html");
                    response.setEntity(file);
                    return;
                }

            }


            raf.seek(raf.length());//先将指针移动到文件末尾,以便追加记录
            byte[] data = username.getBytes("UTF-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            data = password.getBytes("UTF-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            data = nickname.getBytes("UTF-8");
            data = Arrays.copyOf(data, 32);
            raf.write(data);

            raf.writeInt(age);
            System.out.println("注册完毕!");

            //3将注册成功页面设置到response的正文上
            File file = new File("./webapps/myweb/reg_success.html");
            response.setEntity(file);
        }catch(IOException e){
            e.printStackTrace();
        }
        System.out.println("RegServlet:注册处理完毕!");
        }
    }

