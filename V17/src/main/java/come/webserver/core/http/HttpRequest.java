package come.webserver.core.http;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private Socket socket;
    private String merhod;
    private String uri;
    private String protocol;
    private Map<String,String> headers=new HashMap<>();
    private String requestURI;
    private String queryString ;
    private Map<String,String> parameters = new HashMap<>();


    public HttpRequest(Socket socket) throws EmptyRequestException{
        this.socket = socket;
        parseRequestLine();
        parseHeaders();
        parseContent();
    }

    private void parseRequestLine()throws EmptyRequestException{
        System.out.println("HttpRequest:解析请求行...");
        try {
            String line=readLine();
            System.out.println("请求行为:"+line);
            String []data = line.split(" ");
            merhod = data[0];
            uri = data[1];
            protocol = data[2];
            parseUri();
            System.out.println("merhod:"+merhod);
            System.out.println("uri:"+uri);
            System.out.println("protocol:"+protocol);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequest:请求行解析完毕!");

    }

    private  void parseUri() {
        try {
            uri = URLDecoder.decode(uri,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("HttpRequest:进一步解析uri.....");
        if(uri.contains("?")){
            String [] data  = uri.split("\\?");
            requestURI=data[0];
            if(data.length>1){
                queryString=data[1];
                String []arry=queryString.split("&");
                for(String s:arry){
                    String []arrs=s.split("=");
                    String key=arrs[0];
                    String value=arrs[1];
                    if (arrs.length>1){
                        parameters.put(arrs[0],arrs[1]);
                    }else{
                        parameters.put(arrs[0],null);
                    }
                    System.out.println("requestURI:"+requestURI);
                    System.out.println("queryString:"+queryString);
                    System.out.println("parameters:"+parameters);
                    System.out.println("Http进一步解析uri完毕!");
                }
            }
        }
        else {
            requestURI=uri;
        }
    }

    private void parseHeaders(){
        try {
            while (true){
                String line=readLine();
                if(line.isEmpty()){
                    break;
                }
                System.out.print("消息头为:"+line);
                String data[] = line.split(": ");
                headers.put(data[0],data[1]);
                System.out.println();
            }
            System.out.println("headers:"+headers);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void parseContent(){
        System.out.println("HttpRequest:解析消息正文...");

        System.out.println("HttpRequest:消息正文解析完毕!");

    }

    public String readLine() throws IOException{
            InputStream is = socket.getInputStream();
            int d;
            char old = 'a';
            char now = 'a';
            StringBuilder builder = new StringBuilder();
            while ((d = is.read()) != -1) {
                now = (char) d;
                if (old == 13 && now == 10) {
                    break;
                }
                builder.append(now);
                old = now;
            }
        return builder.toString().trim();
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public void setRequestURI(String requestURI) {
        this.requestURI = requestURI;
    }

    public String getParameters(String name) {
        return parameters.get(name);
    }
}
