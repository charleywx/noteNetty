package nia.chapter8;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.oio.OioDatagramChannel;

import java.net.InetSocketAddress;

/**
 * 无连接协议，UDP方式
 * Listing 8.8 Using Bootstrap with DatagramChannel
 */
public class BootstrapDatagramChannel {

    /**
     * Listing 8.8 Using Bootstrap with DatagramChannel
     */
    public void bootstrap() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap
                .group(new OioEventLoopGroup())
                .channel(OioDatagramChannel.class)
                .handler(
                        new SimpleChannelInboundHandler<DatagramPacket>() {
                            @Override
                            public void channelRead0(ChannelHandlerContext ctx,
                                                     DatagramPacket msg) throws Exception {
                                // Do something with the packet
                            }
                        }
                );
        // 因为是无连接的，所以只能调用bind方法
        ChannelFuture future = bootstrap.bind(new InetSocketAddress(0));
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture)
                    throws Exception {
                if (channelFuture.isSuccess()) {
                    System.out.println("Channel bound");
                } else {
                    System.err.println("Bind attempt failed");
                    channelFuture.cause().printStackTrace();
                }
            }
        });
    }
}
