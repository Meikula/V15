package servlet;

import http.HttpRequest;
import http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;

public class LoginServlet extends HttpServlet{
    public void service(HttpRequest request, HttpResponse response){
        String username = request.getParameter("username");
        String psd = request.getParameter("password");
        if(psd==null || username==null){
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);
            return;

        }

        try(RandomAccessFile raf = new RandomAccessFile("user.dat","rw")
        ){
            for(int i=0;i<raf.length()/100;i++){
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name = new String(data,"UTF-8").trim();

                System.out.println(username);
                System.out.println(name);
                if(name.equals(username)){
                    System.out.println("1111111111111111111111"
                    );
                    raf.read(data);
                    String password = new String(data,"UTF-8").trim();

                    if(password.equals(psd)){
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
