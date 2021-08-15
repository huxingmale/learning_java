package server.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import server.netty.adpater.ServerChannelHandlerAdapter;

/**
 * @Description:
 * @ProjectName: week02
 * @Package: server.netty
 * @ClassName: ServerHttpInitializer
 * @Author: huxing
 * @DateTime: 2021-08-15 下午6:17
 */
public class ServerHttpInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        ChannelPipeline pipeline = socketChannel.pipeline();
        pipeline.addLast(new HttpServerCodec());
        pipeline.addLast(new HttpObjectAggregator(1024 * 1024));
        pipeline.addLast(new ServerChannelHandlerAdapter());
    }
}
