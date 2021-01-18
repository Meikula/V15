package http;

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
    private int statusCode = 200;//状态代码,默认值200
    private  String statusReason = "OK";//状态描述,默认值OK
    private File entity;
    private Map<String,String> headers = new HashMap<>();
    private byte[] contextData;
    public HttpResponse(Socket socket){
        this.socket=socket;
    }

    public void flush(){

        try {
            sendStatusLine();
            sendHeaders();
            sendContent();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    public void sendStatusLine(){
        System.out.println("发送状态行....");
        try{
            OutputStream out = socket.getOutputStream();
            String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);

        }catch(IOException e){
            e.printStackTrace();

        }
        System.out.println("状态行发送完毕!");

    }
    public void sendHeaders(){
        System.out.println("发送响应头....");
        try{
            OutputStream out = socket.getOutputStream();
            Set<Map.Entry<String,String>> entry = headers.entrySet();
            for(Map.Entry<String,String> e:entry){
                String name = e.getKey();
                String value = e.getValue();
                String line =name+": "+value;
                out.write(line.getBytes("ISO-8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("响应头:"+line);
            }

            out.write(13);
            out.write(10);

        }catch(IOException e){
            e.printStackTrace();

        }
        System.out.println("响应头发送完毕!");

    }

    public void sendContent(){
        System.out.println("发送响应正文....");
        try{
            if(contextData!=null){
                OutputStream out = socket.getOutputStream();
                out.write(contextData);

            }else if(entity!=null){
                OutputStream out = socket.getOutputStream();
                FileInputStream fis = new FileInputStream(entity);
                int len;
                byte [] data =new byte[1024*10];
                while((len=fis.read(data))!=-1){
                    out.write(data,0,len);

            }

            }

        }catch(IOException e){
            e.printStackTrace();

        }
        System.out.println("响应正文发送完毕!");

    }
    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusReason() {
        return statusReason;
    }

    public void setStatusReason(String statusReason) {
        this.statusReason = statusReason;
    }

    public File getEntity() {
        return entity;
    }

    public void setContextData(byte[] contextData) {
        this.contextData = contextData;
        headers.put("Content-Length",contextData.length+"");
        //putHeader("Content-Length",contextData.length+"");
    }

    public void setEntity(File entity) {
        this.entity = entity;
        int num = entity.getName().lastIndexOf(".")+1;
        String a = entity.getName().substring(num);
        String name= HttpContent.getMimeType(a);
        putHeader("Content-Type",name);
        putHeader("Content-Length",entity.length()+"");
    }

    public void putHeader(String name,String value){
       this.headers.put(name,value);
    }

}
