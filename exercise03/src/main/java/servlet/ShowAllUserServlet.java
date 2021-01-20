package servlet;

import http.HttpRequest;
import http.HttpResponse;
import org.apache.log4j.Logger;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class ShowAllUserServlet extends HttpServlet{
    private Logger logger= Logger.getLogger(ShowAllUserServlet.class);


    List<User> list = new ArrayList<>();

    public void service(HttpRequest request, HttpResponse response){
        logger.info("开始生成动态页面.....");
        try(RandomAccessFile raf = new RandomAccessFile("user.dat","r")){
            for(int i=0;i<raf.length()/100;i++){
                byte[] data = new byte[32];
                raf.read(data);
                String username = new String(data,"UTF-8").trim();

                raf.read(data);
                String password = new String(data,"UTF-8").trim();

                raf.read(data);
                String nickname = new String(data,"UTF-8").trim();

                int age = raf.readInt();

                User user= new User(username,password,nickname,age);
                list.add(user);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        for(User user:list){
            System.out.println(user);
        }

        Context context = new Context();
        context.setVariable("users",list);

        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setTemplateMode("html");
        templateResolver.setCharacterEncoding("UTF-8");

        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver);

        String html = engine.process("./webapps/myweb/userlist.html",context);

        try {
            byte[]data = html.getBytes("UTF-8");
            response.setContentData(data);
            response.putHeader("Content-Type","text/html");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


    }
}
