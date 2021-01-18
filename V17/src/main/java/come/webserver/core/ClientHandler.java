package come.webserver.core;

import come.webserver.core.http.*;

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
            String path = request.getRequestURI();

            File file = new File("./webapps"+path);
            HttpResponse response = new HttpResponse(socket);
            if(path.equals("/myweb/regUser")){
                RegServlet servlet = new RegServlet();
                servlet.service(request,response);


            } else if("/myweb/loginUser".equals(path)) {
                LoginServlet servlet = new LoginServlet();
                servlet.service(request,response);
            }else if("/myweb/showAllUser".equals(path)) {
                ShowAllUserServlet showAllUserServlet =new ShowAllUserServlet();
                showAllUserServlet.service(request,response);
            }
            else {
                if(file.isFile()&&file.exists()){
                    response.setEntity(file);
                }else{
                    File notFile = new File("./webapps/root/404.html");
                    response.setEntity(notFile);
                    response.setStatusCode(404);
                    response.setStatusReason("NotFound");
                    response.putHeader("Content-Type","text/html");
                    response.putHeader("Content-Length",notFile.length()+"");
                }

            }
            response.putHeader("Sever","WebServer");
            response.flush();
            System.out.println("响应发送完毕");


        } catch (EmptyRequestException e){

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
