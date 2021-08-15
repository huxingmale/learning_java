package service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @Description: SOCKET监听线程
 * @ProjectName: week02
 * @Package: PACKAGE_NAME
 * @ClassName: service.SocketRunable
 * @Author: huxing
 * @DateTime: 2021-08-14 下午6:08
 */
public class SocketRunable implements Runnable{

    public final static String HELLO_1 = "Hello, nio1";

    public final static String HELLO_2 = "Hello, nio2";

    public final static String HELLO_3 = "Hello, nio3";

    /** socket 套接字 **/
    private final Socket socket;

    /** 发送消息内容 **/
    public String body;

    public SocketRunable(Socket socket, String body){
        this.socket = socket;
        this.body = body;
    }

    @Override
    public void run() {
        SocketRunable.service(socket, body);
    }

    /**
     * @Description:
     * @Author: huxing
     * @param socket
     * @Date: 2021/8/14 下午8:50
     **/
    public static void service(Socket socket, String body){
        try {
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("HTTP/1.1 200 OK");
            printWriter.println("Content-type:text/html;charset=utf-8");
            printWriter.println("Content-Length:" + body.length());
            printWriter.println();
            printWriter.write(body);
            printWriter.close();
            socket.close();
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }
}
