package server.http;

import service.SocketRunable;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: HttpServerTest
 * @ProjectName: week02
 * @Package: PACKAGE_NAME
 * @ClassName: HttpServerTest
 * @Author: huxing
 * @DateTime: 2021-08-14 下午5:56
 */
public class HttpServer03 {

    public static void main(String[] args) throws Exception{
       new HttpServer03().starServer();
    }

    public void starServer(){
        try {
            ExecutorService executorService = Executors.newFixedThreadPool(
                    Runtime.getRuntime().availableProcessors() * 4);
            final ServerSocket serverSocket = new ServerSocket(8803);
            System.out.println("启动http Server服务1，端口号：" + 8803);
            while (true){
                Socket socket = serverSocket.accept();
                executorService.execute(()-> SocketRunable.service(socket,
                        SocketRunable.HELLO_3));
            }
        } catch (Exception ex){
            ex.printStackTrace();
            System.out.println("启动http Server服务1失败，端口号：" + 8803);
        }
    }
}
