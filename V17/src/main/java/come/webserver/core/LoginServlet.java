package come.webserver.core;

import come.webserver.core.http.HttpRequest;
import come.webserver.core.http.HttpResponse;

import java.io.File;
import java.io.RandomAccessFile;

public class LoginServlet {
    public void service(HttpRequest request, HttpResponse response){
        String username = request.getParameters("username");
        String password = request.getParameters("password");
        if(username==null || password==null){
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);
            return;
        }

        try(RandomAccessFile raf = new RandomAccessFile("user.dat","r");
        ) {
            for(int i=0;i<raf.length()/100;i++){
                //现将指针移动到该条记录的开始位置
                raf.seek(i*100);
                byte[] data = new byte[32];
                raf.read(data);
                String name= new String(data,"UTF-8").trim();
                if(name.equals(username)){
                    //找到该用户,此时再比对密码
                    byte[] data1 = new byte[32];
                    raf.read(data1);
                    String pwd = new String(data1,"UTF-8").trim();
                    if(pwd.equals(password)){
                        //登陆成功
                        File file = new File("./webapps/myweb/login_success.html");
                        response.setEntity(file);
                        return;
                    }
                    //执行到这里说明用户名对了,但是密码不对
                    break;//停止读取工作,因为没有重复记录
                }

            }

            //设置登陆失败页面
            File file = new File("./webapps/myweb/login_fail.html");
            response.setEntity(file);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
