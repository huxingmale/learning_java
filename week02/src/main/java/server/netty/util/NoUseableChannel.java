package server.netty.util;

/**
 * @Description:
 * @ProjectName: week02
 * @Package: server.netty.util
 * @ClassName: NoUseableChannel
 * @Author: huxing
 * @DateTime: 2021-08-15 下午6:40
 */
public class NoUseableChannel extends RuntimeException{

    private static final long serialVersionUID = 7762465537123947683L;

    public NoUseableChannel() {
        super();
    }

    public NoUseableChannel(String message) {
        super(message);
    }
}
