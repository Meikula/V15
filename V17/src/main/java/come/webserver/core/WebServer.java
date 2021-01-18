package come.webserver.core;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    private ServerSocket serverSocket;
    public WebServer(){
        System.out.println("正在启动服务端....");
        try {
            serverSocket = new ServerSocket(8088);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("服务端启动完毕");

    }

    public void start(){
        try {
            while (true) {
                System.out.println("正在连接客户端....");
                Socket socket = serverSocket.accept();
                System.out.println("一个客户端已连接!");
                ClientHandler clientHandler = new ClientHandler(socket);
                Thread t = new Thread(clientHandler);
                t.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebServer webServer = new WebServer();
        webServer.start();

    }
}
