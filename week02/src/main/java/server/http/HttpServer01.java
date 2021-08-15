package server.http;

import service.SocketRunable;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description: HttpServerTest
 * @ProjectName: week02
 * @Package: PACKAGE_NAME
 * @ClassName: HttpServerTest
 * @Author: huxing
 * @DateTime: 2021-08-14 下午5:56
 */
public class HttpServer01 {

    public static void main(String[] args) throws Exception{
       new HttpServer01().starServer();
    }

    public void starServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(8801);
            System.out.println("启动http Server服务1，端口号：" + 8801);
            while (true){
                Socket socket = serverSocket.accept();
                SocketRunable.service(socket, SocketRunable.HELLO_1);
            }
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("启动http Server服务1失败，端口号：" + 8801);
        }
    }
}
