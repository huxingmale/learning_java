import server.http.HttpServer01;
import server.http.HttpServer02;

/**
 * @Description:
 * @ProjectName: week02
 * @Package: PACKAGE_NAME
 * @ClassName: HttpServerStart
 * @Author: huxing
 * @DateTime: 2021-08-14 下午6:13
 */
public class HttpServerStart {

    public static void main(String[] args) throws Exception {
        Thread thread1 = new Thread(){
            @Override
            public void run() {
                System.out.println("启动第一个socket server服务");
                new HttpServer01().starServer();
            }
        };
        Thread thread2 = new Thread(){
            @Override
            public void run() {
                System.out.println("启动第二个socket server服务");
                new HttpServer02().starServer();
            }
        };
        thread1.start();
        thread2.start();
    }
}
