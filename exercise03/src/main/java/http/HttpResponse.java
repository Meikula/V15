package http;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpResponse {
    private Socket socket;
    private File sFile;
    private String name = "OK";
    private int num = 200;
    private byte[]contentData;
    Map<String,String> map = new HashMap<>();



    public HttpResponse(Socket socket) {
        this.socket = socket;
    }
    public void flush(){

        try {
            OutputStream out = socket.getOutputStream();
            sendStatusLine();
            sendHeaders();
            sendContent();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void sendStatusLine(){
        try {
            OutputStream out = socket.getOutputStream();
            //String line = "HTTP/1.1 200 OK";
            String line = "HTTP/1.1"+" "+num+" "+name;
            out.write(line.getBytes("ISO-8859-1"));
            out.write(13);
            out.write(10);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void sendHeaders(){
        try {

            OutputStream out = socket.getOutputStream();
            Set<Map.Entry<String,String>> entrySet = map.entrySet();
            for(Map.Entry<String,String>e:entrySet){
                String key = e.getKey();
                String value = e.getValue();
                //out.write(line.getBytes("ISO-8859-1"));
                String line = key+": "+value;
                out.write(line.getBytes("ISO-8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("响应头:"+line);

            }
            out.write(13);
            out.write(10);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendContent(){
        if(contentData!=null){
            try {
                OutputStream out = socket.getOutputStream();
                out.write(contentData);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else if(sFile!=null) {
            try {
                OutputStream out = socket.getOutputStream();
                int len;
                byte[] data = new byte[1024 * 10];
                FileInputStream fis = new FileInputStream(sFile);
                while ((len = fis.read(data)) != -1) {
                    out.write(data, 0, len);
                }
                System.out.println("响应正文发送完毕");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public File getsFile() {
        return sFile;
    }

    public void setsFile(File sFile) {
        this.sFile = sFile;

        int index = sFile.getName().lastIndexOf(".")+1;
        String str = sFile.getName().substring(index);
        String l = HttpContent.getMimeType(str);
        String f = "Content-Type";
        putHeader(f,l);
        putHeader("Content-Length",sFile.length()+"");

    }
    public void setName(String name) {
        this.name = name;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public void putHeader(String key,String value){
        map.put(key,value);
    }

    public void setContentData(byte[] contentData) {
        this.contentData = contentData;
        map.put("Content-Length",contentData.length+"");

    }
}
