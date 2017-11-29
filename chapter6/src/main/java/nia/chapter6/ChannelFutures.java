package nia.chapter6;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;


/**
 * Channel的生命周期
 * ChannelRegistered -> ChannelActive -> ChannelInactive -> ChannelUnRegistered
 * 当Channel的状态改变时，会生成对应的事件转发给ChannelPipeline中的ChannelHandler来处理。
 * <p>
 * ChannelHandler 生命周期
 * handlerAdded 当ChannelHandler添加到ChannelPipeline中时被调用
 * handlerRemove 当ChannelHandler从ChannelPipeline中移除时被调用
 * exceptionCaught 处理过程中ChannelPipeline中有错误时被调用
 * <p>
 * ChannelHandlerAdapter.isSharable() 返回True表示已经添加到多个ChannelPipeline
 * <p>
 * <p>
 * <p>
 * Listing 6.13 Adding a ChannelFutureListener to a ChannelFuture
 */
public class ChannelFutures {
    private static final Channel CHANNEL_FROM_SOMEWHERE = new NioSocketChannel();
    private static final ByteBuf SOME_MSG_FROM_SOMEWHERE = Unpooled.buffer(1024);

    /**
     * Listing 6.13 Adding a ChannelFutureListener to a ChannelFuture
     */
    public static void addingChannelFutureListener() {
        Channel channel = CHANNEL_FROM_SOMEWHERE; // get reference to pipeline;
        ByteBuf someMessage = SOME_MSG_FROM_SOMEWHERE; // get reference to pipeline;
        //...
        io.netty.channel.ChannelFuture future = channel.write(someMessage);
        // 出站操作结果处理，成功或者失败
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(io.netty.channel.ChannelFuture f) {
                if (!f.isSuccess()) {
                    f.cause().printStackTrace();
                    f.channel().close();
                }
            }
        });
    }
}
