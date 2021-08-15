package server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Description: Netty服务监听
 * @ProjectName: week02
 * @Package: server.netty
 * @ClassName: NettyServerListener
 * @Author: huxing
 * @DateTime: 2021-08-15 下午5:49
 */
public class NettyServerListener {

    /**
     * NettyServerListener 日志输出器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerListener.class);

    /**
     * 创建bootstrap
     */
    private ServerBootstrap serverBootstrap = new ServerBootstrap();

    /**
     * BOSS
     */
    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    /**
     * Worker
     */
    private EventLoopGroup workGroup = new NioEventLoopGroup();

    public void start(){
        try {
            // 初始化基础
            serverBootstrap.option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
            // 设置处理流程
            serverBootstrap.group(bossGroup, workGroup).channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ServerHttpInitializer());
            // 取本机IP地址
            String address = getLocalHost();
            LOGGER.info("POSP服务(POS接入)启动成功");
            LOGGER.info("服务器IP: {}, 端口: [{}]", address, 8808);
            ChannelFuture future = serverBootstrap.bind(8808).sync();
            future.channel().closeFuture().sync();
        } catch (Exception ex){
            LOGGER.info(ex.getMessage());
            LOGGER.info("[出现异常] 释放资源");
            LOGGER.error("POSP服务(POS接入)启动失败", ex);
            this.close();
        }
    }

    /**
     * @Description: 关闭服务器
     * @Author: huxing
     * @param
     * @Date: 2021/8/15 下午6:14
     **/
    public void close() {
        LOGGER.info("关闭服务器....");
        //优雅退出
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
    }

    /**
     * @Description: 获取本机ip地址
     * @Author: huxing
     * @param
     * @return java.lang.String
     * @Date: 2021/8/15 下午6:14
     **/
    public static String getLocalHost() throws SocketException {
        Enumeration allNetInterfaces = NetworkInterface.getNetworkInterfaces();
        InetAddress ip = null;
        while (allNetInterfaces.hasMoreElements())
        {
            NetworkInterface netInterface = (NetworkInterface) allNetInterfaces.nextElement();
            System.out.println(netInterface.getName());
            if (!isValidInterface(netInterface)) {
                continue;
            }
            Enumeration addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements())
            {
                ip = (InetAddress) addresses.nextElement();
                if (isValidAddress(ip))
                {
                    System.out.println("本机的IP = " + ip.getHostAddress());
                    return ip.getHostAddress();
                }
            }
        }
        return null;
    }

    /**
     * 过滤回环网卡、点对点网卡、非活动网卡、虚拟网卡并要求网卡名字是eth或ens开头
     *
     * @param ni 网卡
     * @return 如果满足要求则true，否则false
     */
    private static boolean isValidInterface(NetworkInterface ni) throws SocketException {
        return !ni.isLoopback() && !ni.isPointToPoint() && ni.isUp() && !ni.isVirtual()
                && (ni.getName().startsWith("eth") || ni.getName().startsWith("en"));
    }

    /**
     * 判断是否是IPv4，并且内网地址并过滤回环地址.
     */
    private static boolean isValidAddress(InetAddress address) {
        return address instanceof Inet4Address && address.isSiteLocalAddress() && !address.isLoopbackAddress();
    }

    public static void main(String[] args) {
        new NettyServerListener().start();
    }
}
