package com.webserver.http;
/**
 * 响应对象,当前类的每一个实例用于表示服务端发送给客户端的一个标准的
 * HTTP响应内容
 * 没给响应由三部分构成:状态行,响应头,响应正文
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class HttpResponse {
    private int statusCode = 200;//状态代码,默认值200
    private  String statusReason = "OK";//状态描述,默认值OK
    private Map<String,String> headers = new HashMap<>();
    private File entity;
    private Socket socket;

    public HttpResponse(Socket socket){
        this.socket=socket;

    }

    /**
     * 将当前响应对象内容以标准的HTTP响应格式发送给客户端
     */
    public void flush() {
        try {

            OutputStream out = socket.getOutputStream();
            //1发送状态行
            sendStatusLine();
            //2发送响应头
            sendHeaders();
            //3发送响应正文
            sendContent();

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    //发送状态行
    private void sendStatusLine(){
        System.out.println("HttpResponse:开始发送状态行....");
        try{
            //1发送状态行
            OutputStream out = socket.getOutputStream();
            String line = "HTTP/1.1"+" "+statusCode+" "+statusReason;
            out.write(line.getBytes("ISO8859-1"));
            out.write(13);//发送一个回车符
            out.write(10);//发送一个换行符

        }catch (IOException e){
            e.printStackTrace();

        }
        System.out.println("HttpResponse:状态行发送完毕!");
    }
    //发送响应头
    private void sendHeaders(){
        System.out.println("HTTPResponse:开始发送响应头.....");
        try{

            OutputStream out = socket.getOutputStream();
            Set<Map.Entry<String,String>> set = headers.entrySet();
            for(Map.Entry<String,String> e:set){
                String name = e.getKey();
                String value = e.getValue();
                String line = name + ": "+value;
                out.write(line.getBytes("ISO-8859-1"));
                out.write(13);
                out.write(10);
                System.out.println("响应头:"+line);
            }
            //单独发送CRLF表示响应头发送完毕
            out.write(13);//发送一个回车符
            out.write(10);//发送一个换行符

        }catch (IOException e){
            e.printStackTrace();
        }

        System.out.println("HTTPResponse:响应头发送完毕!");

    }
    //发送响应正文
    private void sendContent(){
        System.out.println("HTTPResponse:开始发送响应正文....");
        try{
            OutputStream out = socket.getOutputStream();
            FileInputStream fis = new FileInputStream(entity);
            int len;//表示每次实际读取到的字节数
            byte[] data = new byte[1024*10];
            while ((len=fis.read(data))!=-1){
                out.write(data,0,len);
            }
        }catch (IOException e){
            e.printStackTrace();

        }
        System.out.println("HTTPResponse:响应正文发送完毕!");
    }

    public File getEntity() {
        return entity;
    }

    public void setEntity(File entity) {
        this.entity = entity;
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
    /*
        添加一个响应头
        name响应头名字
        value 响应头的值
     */
    public void putHeader(String name,String value){
        this.headers.put(name,value);
    }




}
