package come.webserver.core.http;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {
    private Socket socket;
    private File entity;
    private int statusCode = 200;//状态代码,默认值200
    private  String statusReason = "OK";
    Map<String,String> headers = new HashMap<>();
    private byte[] contentData;

    public HttpResponse(Socket socket) {
        this.socket = socket;
    }
    public void flush(){
        sendStatusLine();
        sendHeaders();
        sendContent();


    }
    private void sendStatusLine(){
        try {
            OutputStream out = socket.getOutputStream();
            String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private  void sendHeaders(){
        try { OutputStream out = socket.getOutputStream();
            Set<Map.Entry<String,String>> set = headers.entrySet();
            for(Map.Entry<String,String> e:set){
                String key = e.getKey();
                String value = e.getValue();
                String line = key+": "+value;
                //String line = "Content-Type: text/html";
                out.write(line.getBytes("ISO8859-1"));
                out.write(13);//发送一个回车符
                out.write(10);//发送一个换行符
                System.out.println("响应头:"+line);
            }
            //line = "Content-Length: "+entity.length();
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendContent(){
        System.out.println("HTTPResponse:开始发送响应正文....");
        if(contentData!=null){
            try {
                OutputStream out = socket.getOutputStream();
                out.write(contentData);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(entity!=null) {
            try(
                    FileInputStream fis = new FileInputStream(entity);
            ){
                OutputStream out = socket.getOutputStream();

                int len;//表示每次实际读取到的字节数
                byte[] data = new byte[1024 * 10];
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("HTTPResponse:响应正文发送完毕!");

    }

    public void setEntity(File entity) {
        this.entity = entity;
        int index=entity.getName().lastIndexOf(".")+1;
        String key = entity.getName().substring(index);
        String value = HttpContext.getMimeType(key);
        putHeader("Content-Type",value);
        putHeader("Content-Length",entity.length()+"");
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }
    public void putHeader(String key,String value){
        headers.put(key,value);
    }
    public void setContentData(byte[] contentData) {
        this.contentData = contentData;
        putHeader("Content-Length",contentData.length+"");

    }
}
