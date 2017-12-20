package nia.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

/**
 * 出站消息
 * Listing 6.4 Discarding and releasing outbound data
 */
@Sharable
public class DiscardOutboundHandler
        extends ChannelOutboundHandlerAdapter {
    @Override
    public void write(ChannelHandlerContext ctx,
                      Object msg, ChannelPromise promise) {
        // 释放资源
        ReferenceCountUtil.release(msg);
        // 通知数据已经被处理
        promise.setSuccess();
    }
}

