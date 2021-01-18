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
    private Map<String, String> map = new HashMap<>();

    public HttpRequest(Socket socket) throws EmptyRequestException {
        this.socket = socket;
        parseRequestLine();
        parseHeaders();
        parseContent();

    }

    private void parseRequestLine() throws EmptyRequestException{
        System.out.println("HttpRequest:解析请求行...");
        try {
            String line = readLine();
            System.out.print("请求行:" + line);
            if(line.isEmpty()){
                throw new EmptyRequestException();
            }
            String[] data = line.split(" ");
            method = data[0];
            uri = data[1];
            protocol = data[2];

            parseUri();

            System.out.println("merhod:" +method);
            System.out.println("uri:" +uri);
            System.out.println("protocol:" +protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequest:请求行解析完毕!");

    }
    private void parseUri(){
        try {
            uri=URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if(uri.contains("?")){
            String data [] = uri.split("\\?");
            requestURI = data[0];
            if(data.length>1){
                queryString = data[1];
                String arr[] = queryString.split("&");
                for(String s:arr){
                    String [] arry=s.split("=");
                    if(arry.length>1){
                        parameters.put(arry[0],arry[1]);
                    }else{
                        parameters.put(arry[0],null);
                    }
                }

            }

        }else{
            requestURI = uri;
        }
    }

    private void parseHeaders() {
        System.out.println("HttpRequest:解析消息头...");
        try {

            while (true) {
                String line = readLine();
                if (line.isEmpty()) {
                    break;
                }
                String data1[] = line.split("\\s");
                map.put(data1[0], data1[1]);
            }
            System.out.println("header:" + map);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseContent() {
        System.out.println("HttpRequest:解析消息正文...");

        System.out.println("HttpRequest:消息正文解析完毕!");

    }

    private String readLine()throws IOException {
        InputStream in = socket.getInputStream();
        int d;
        char old = 'a';
        char now = 'a';
        StringBuilder builder = new StringBuilder();
        while ((d = in.read()) != -1) {
            now = (char) d;
            if (old == 13 && now == 10) {
                break;
            }
            builder.append(now);
            old = now;
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
        return map.get(name);
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
