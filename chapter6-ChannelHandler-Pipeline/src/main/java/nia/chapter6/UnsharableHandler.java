package nia.chapter6;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Listing 6.11 Invalid usage of @Sharable
 */
@Sharable // 确保ChannelHandler是线程安全的才能使用该标签 表示：该ChannelHandler可以绑定多个ChannelHandlerContext实例
public class UnsharableHandler extends ChannelInboundHandlerAdapter {
    private int count;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 计数器+1
        count++;
        System.out.println("inboundBufferUpdated(...) called the "
                + count + " time");
        // 记录并转发给下一个ChannelHandler
        ctx.fireChannelRead(msg);
    }
}

