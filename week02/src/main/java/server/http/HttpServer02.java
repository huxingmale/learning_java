package server.http;

import service.SocketRunable;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description:
 * @ProjectName: week02
 * @Package: PACKAGE_NAME
 * @ClassName: server.htpp.HttpServer02
 * @Author: huxing
 * @DateTime: 2021-08-14 下午6:10
 */
public class HttpServer02 {

    public static void main(String[] args) throws Exception{
        new HttpServer02().starServer();
    }

    public void starServer(){
        try {
            ServerSocket serverSocket = new ServerSocket(8802);
            System.out.println("启动http Server服务2，端口号：" + 8802);
            while (true){
                Socket socket = serverSocket.accept();
                // 创建线程并执行
                new Thread(() -> SocketRunable.service(socket,
                        SocketRunable.HELLO_2)).start();
            }
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("启动http Server服务2失败，端口号：" + 8802);
        }
    }
}
