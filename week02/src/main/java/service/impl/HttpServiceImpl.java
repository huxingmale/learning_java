package service.impl;

import com.sun.org.apache.regexp.internal.RE;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import service.HttpService;
import sun.lwawt.macosx.CSystemTray;
import sun.net.www.http.KeepAliveCache;

import java.nio.charset.StandardCharsets;

import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.NO_CONTENT;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * @Description:
 * @ProjectName: week02
 * @Package: service.impl
 * @ClassName: HttpServiceImpl
 * @Author: huxing
 * @DateTime: 2021-08-15 上午11:58
 */
public class HttpServiceImpl implements HttpService {

    @Override
    public void handlerTest(FullHttpRequest request, ChannelHandlerContext ctx, String body) {
        FullHttpResponse response = null;
        try {
            System.out.println("返回的记录信息: " + body);
            response = new DefaultFullHttpResponse(HTTP_1_1, OK,
                    Unpooled.wrappedBuffer(body.getBytes(StandardCharsets.UTF_8)));
            response.headers().set("Content-Type", "application/json");
            response.headers().setInt("Content-Length", response.content().readableBytes());
        } catch (Exception ex){
            System.out.println("处理出错: " + ex.getMessage());
            response = new DefaultFullHttpResponse(HTTP_1_1, NO_CONTENT);
        } finally {
            if (request != null){
                if(!HttpUtil.isKeepAlive(request)){
                    ctx.write(response).addListener(ChannelFutureListener.CLOSE);
                } else {
                    response.headers().set(CONNECTION, KEEP_ALIVE);
                    ctx.write(response);
                }
            }
            ctx.flush();
        }
    }
}
