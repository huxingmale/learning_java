package service;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

/**
 * @Description: 业务实现
 * @ProjectName: week02
 * @Package: service
 * @ClassName: BusinessService
 * @Author: huxing
 * @DateTime: 2021-08-15 上午11:57
 */
public interface HttpService {

    /**
     * @Description:
     * @Author: huxing
     * @param request
     * @param cx
     * @param body
     * @return java.lang.String
     * @Date: 2021/8/15 下午6:28
     **/
    void handlerTest(FullHttpRequest request, ChannelHandlerContext cx, String body);
}
