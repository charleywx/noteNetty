package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.Future;

import java.net.InetSocketAddress;

/**
 * Listing 8.9 Graceful shutdown
 */
public class GracefulShutdown {
    public static void main(String args[]) {
        GracefulShutdown client = new GracefulShutdown();
        client.bootstrap();
    }

    /**
     * Listing 8.9 Graceful shutdown
     */
    public void bootstrap() {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                //...
                .handler(
                        new SimpleChannelInboundHandler<ByteBuf>() {
                            @Override
                            protected void channelRead0(
                                    ChannelHandlerContext channelHandlerContext,
                                    ByteBuf byteBuf) throws Exception {
                                System.out.println("Received data");
                            }
                        }
                );
        bootstrap.connect(new InetSocketAddress("www.manning.com", 80)).syncUninterruptibly();
        // 处理任何挂起的事件和任务，释放所有活动的线程资源，关闭正在使用中的Channel
        Future<?> future = group.shutdownGracefully();
        // block until the group has shutdown 阻塞直到group关闭
        future.syncUninterruptibly();
    }
}
