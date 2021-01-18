package http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private Socket socket;
    private String merhot;
    private String uri;
    private String protocol;
    private String requestURI;
    private String queryString;
    Map<String,String> parameters = new HashMap<>();
    Map<String,String> headers=new HashMap<>();
    public HttpRequest(Socket socket) throws EmptyRequestException{
        this.socket=socket;
        parseRequestLine();
        parseHeaders();
        parseContent();
    }
    private void parseRequestLine() throws EmptyRequestException{
        System.out.println("正在解析请求行..");
        try {
            String str=readline();
            System.out.println("请求行:"+str);
            String[] data = {};
            data=str.split("\\s");
            merhot = data[0];
            uri = data[1];
            protocol = data[2];

            parseURI();

            System.out.println("merhot:"+merhot);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("请求行解析完毕!");

    }
    private void parseURI(){
        try {
            uri= URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(uri.contains("?")){
            String data [] = uri.split("\\?");
            requestURI = data[0];
            if(data.length>1){
                queryString = data[1];
                String arr [] = queryString.split("&");
                for(String s : arr){
                    String name []= s.split("=");
                    if(name.length>1){
                        parameters.put(name[0],name[1]);
                    }else{
                        parameters.put(name[0],null);
                    }
                }

            }

        }else {
        requestURI = uri;
        }

        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);
        System.out.println("Http进一步解析uri完毕!");
    }




    private void parseHeaders(){
        System.out.println("解析消息头...");
        try {

            while(true){
                String s=readline();
                if(s.isEmpty()){
                    break;
                }
                System.out.println("消息头:" + s);

                String [] arry = s.split("\\:\\s");
                String key = arry[0];
                String value = arry[1];
                headers.put(arry[0],arry[1]);
            }
            System.out.println("heads:"+headers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("消息头解析完毕!");


    }
    private void parseContent(){
        System.out.println("HttpRequest:解析消息正文...");

        System.out.println("HttpRequest:消息正文解析完毕!");
    }

    public String readline() throws IOException {
        InputStream in = socket.getInputStream();
        int line;
        StringBuilder builder = new StringBuilder();
        char old='a';
        char now='a';
        while((line=in.read())!=-1){
            now=(char)line;
            if(old==13&&now==10){
                break;
            }
            builder.append(now);
            System.out.print((char)line);
            old=now;
        }
        String str=new String(builder).trim();
        return str;

    }
    public String getMerhot() { return merhot; }

    public String getUri() { return uri; }

    public String getProtocol() { return protocol; }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameter(String name) {
        return parameters.get(name);
    }
}
