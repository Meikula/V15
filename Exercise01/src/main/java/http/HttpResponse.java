package http;

import sun.invoke.empty.Empty;

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
    private int statusCode = 200;
    private  String statusReason = "OK";
    private File entity;
    private byte[]contentData;
    Map<String,String> map = new HashMap<>();

    public HttpResponse(Socket socket){
        this.socket=socket;
    }

    public void flush(){
        try {
            OutputStream out = socket.getOutputStream();
            sendStatusLine();
            sendHeaders();
            sendContent();
        }catch (IOException e){
            e.printStackTrace();
        }
        finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
    private void sendStatusLine(){
        try{
            OutputStream out = socket.getOutputStream();
            //String line = "HTTP/1.1 200 OK";
            String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);
            out.write(10);

        }catch(IOException e){
            e.printStackTrace();
        }

    }
    private void sendHeaders(){
        try{

                OutputStream out = socket.getOutputStream();
                Set<Map.Entry<String,String>> entrySet = map.entrySet();
                for(Map.Entry<String,String> e:entrySet){
                    String key = e.getKey();
                    String value = e.getValue();
                    String line = key+": "+value;
                    out.write(line.getBytes("ISO8859-1"));
                    out.write(13);
                    out.write(10);
                    System.out.println("响应头是:"+line);


            }
            out.write(13);
            out.write(10);


        }catch(IOException e){
            e.printStackTrace();
        }

    }
    private void  sendContent() {
        if (contentData != null) {
            try {
                OutputStream out = socket.getOutputStream();
                out.write(contentData);
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (entity != null) {
            try {
                OutputStream out = socket.getOutputStream();
                FileInputStream fis = new FileInputStream(entity);
                int len;
                byte[] data = new byte[1024 * 10];
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);
                }
                System.out.println("响应发送完毕");

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    public void setContentData(byte[] contentData) {
        this.contentData = contentData;
        map.put("Content-Length",contentData.length+"");

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

    public void setEntity(File entity) {
        this.entity = entity;
        int index = entity.getName().lastIndexOf(".")+1;
        String str = entity.getName().substring(index);
        String value= HttpContext.getMimeType(str);
        String name = "Content-Type";
        putHeader(name,value);
        putHeader("Content-Length",entity.length()+"");
    }

    public void putHeader(String key,String value){
        this.map.put(key,value);

    }
}
