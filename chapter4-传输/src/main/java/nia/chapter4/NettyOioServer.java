package nia.chapter4;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Netty版的OIO 阻塞网络处理
 * <p>
 * Listing 4.3 Blocking networking with Netty
 */
public class NettyOioServer {
    public void server(int port)
            throws Exception {
        final ByteBuf buf =
                Unpooled.unreleasableBuffer(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
        // 为阻塞模式使用的控制流处理
        EventLoopGroup group = new OioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(group)
                    // 使用OioEventLoopGroup以允许阻塞模式，旧的IO
                    .channel(OioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    // 指定ChannelInitializer，每个已接受的连接都会调用它
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    // 添加一个ChannelInboundHandlerAdapter以拦截和处理事件
                                    new ChannelInboundHandlerAdapter() {
                                        @Override
                                        public void channelActive(
                                                ChannelHandlerContext ctx)
                                                throws Exception {
                                            ctx.writeAndFlush(buf.duplicate())
                                                    .addListener(
                                                            // 将消息写到客户端，并添加ChannelFutureListener，以便消息一写完就关闭连接
                                                            ChannelFutureListener.CLOSE);
                                        }
                                    });
                        }
                    });
            // 绑定服务器以接受连接
            ChannelFuture f = b.bind().sync();
            f.channel().closeFuture().sync();
        } finally {
            // 释放所有的资源
            group.shutdownGracefully().sync();
        }
    }
}

