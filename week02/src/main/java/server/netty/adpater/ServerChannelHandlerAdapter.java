package server.netty.adpater;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.netty.util.ChannelUtil;
import service.HttpService;
import service.impl.HttpServiceImpl;

/**
 * @Description:
 * @ProjectName: week02
 * @Package: server.netty.adpater
 * @ClassName: ServerChannelHandlerAdapter
 * @Author: huxing
 * @DateTime: 2021-08-15 下午6:19
 */
public class ServerChannelHandlerAdapter extends ChannelInboundHandlerAdapter {

    /** 日志函数 **/
    private final static Logger log = LoggerFactory.getLogger(ServerChannelHandlerAdapter.class);

    private HttpService service = new HttpServiceImpl();

    /** 心跳消息 **/
    private int lossConnectCount = 0;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.info("{} -> [连接异常] {}通道异常，异常原因：{}", this.getClass().getName(),
                ctx.channel().id(), cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext channelHandlerContext) throws Exception {
        log.info("{} -> [连接建立成功] {}", this.getClass().getName(),
                channelHandlerContext.channel().id());
    }

    /**
     * 服务器接收到消息时进行进行的处理
     *
     * @param ctx   ctx
     * @param msg   msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 注册通道
        ChannelUtil.registerChannel(ctx.channel());
        try {
            FullHttpRequest request = (FullHttpRequest) msg;
            String url = request.uri();
            if (url.contains("/test")){
                this.service.handlerTest(request, ctx, "Hello, Huxing");
            } else {
                this.service.handlerTest(request, ctx, "Hello, Others");
            }
        } catch (Exception ex){
            ex.printStackTrace();
        } finally {
            // 这里是把消息给释放掉了不然会重复调用
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    /**
     * 触发器
     *
     * @param channelHandlerContext channelHandlerContext
     * @param evt
     * @throws Exception exception
     */
    @Override
    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object evt) throws Exception {
        log.info("{} -> [已经有5秒中没有接收到客户端的消息了]", this.getClass().getName());
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (idleStateEvent.state() == IdleState.READER_IDLE) {
                lossConnectCount++;
                if (lossConnectCount > 2) {
                    log.info("{} -> [释放不活跃通道] {}", this.getClass().getName(), channelHandlerContext.channel().id());
                    channelHandlerContext.channel().close();
                }
            }
        } else {
            super.userEventTriggered(channelHandlerContext, evt);
        }
    }
}
