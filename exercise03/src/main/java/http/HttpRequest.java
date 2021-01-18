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
    private String method;
    private String uri;
    private String protocol;
    private String requestURI;
    private String queryString;
    private Map<String,String> parameters = new HashMap<>();
    private Map<String,String> headers = new HashMap<>();



    public HttpRequest(Socket socket) throws EmptyRequestException{
        this.socket = socket;
        parseRequestLine();
        parseHeaders();
        parseContent();
    }
    private void parseRequestLine()throws EmptyRequestException{
        try{
            String line = readLine();
            if(line.isEmpty()){
                throw new EmptyRequestException();
            }
            System.out.println("状态行:"+line);
            String arr[] = line.split(" ");
            method = arr[0];
            uri = arr[1];
            protocol = arr[2];

            parseURI();



            System.out.println("merhod:"+method);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);

        }catch(IOException e){
            e.printStackTrace();
        }

    }
    private  void parseURI(){
        try {
            uri=URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(uri.contains("?")){
            String data[] = uri.split("\\?");
            requestURI = data[0];
            if(data.length>1){
                queryString = data[1];
                String arr[] = queryString.split("&");
                for(String str:arr){
                    String [] name=str.split("=");
                    if(name.length>1){
                        parameters.put(name[0],name[1]);
                    }else {
                        parameters.put(name[0],null);
                    }
                }
            }
            System.out.println("HttpRequest:进一步解析uri完毕!");


        }else{
            requestURI=uri;
        }
        System.out.println("requestURI:"+requestURI);
        System.out.println("queryString:"+queryString);
        System.out.println("parameters:"+parameters);
        System.out.println("Http进一步解析uri完毕!");


    }
    private void parseHeaders(){
        try{

            while (true){
                String line=readLine();
                if(line.isEmpty()){
                    break;
                }
                System.out.println("消息头:"+line);
                String data [] = line.split(": ");
                headers.put(data[0],data[1]);
            }
            System.out.println("headers:"+headers);

        }catch(IOException e){
            e.printStackTrace();
        }

    }
    private void parseContent(){
        System.out.println("HttpRequest:解析消息正文...");

        System.out.println("HttpRequest:消息正文解析完毕!");

    }
    public String readLine()throws IOException{
        InputStream in = socket.getInputStream();
        int d;
        char now = 'a';
        char old = 'a';
        StringBuilder builder = new StringBuilder();
        while ((d=in.read())!=-1){
            now=(char)d;
            if(old==13&&now==10){
                break;
            }
            builder.append(now);
            old=now;
        }
        String line = builder.toString().trim();
        return line;

    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getHeader(String name) {
        return headers.get(name);
    }

    public String getRequestURI() {
        return requestURI;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getParameters(String name) {
        return parameters.get(name);
    }
}
