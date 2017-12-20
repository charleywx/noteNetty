package gps.dispatcher.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class Server {
    
    public final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ConcurrentLinkedQueue queue;
    
    private int port = 18188;
    
    public static void main(String[] args) {
        Server server = new Server();
        server.start();
    }
    
    public void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(NioServerSocketChannel.class)
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                                @Override
                                public void channelRead(ChannelHandlerContext ctx, Object msg) {
                                    ByteBuf in = (ByteBuf) msg;
                                    System.out.println(
                                            "服务端接受消息: " + in.toString(CharsetUtil.UTF_8));
                                    // 将接收到的消息写给发送者，而不冲刷出站消息
                                    ctx.write(in);
                                }
                                
                                @Override
                                public void channelReadComplete(ChannelHandlerContext ctx)
                                        throws Exception {
                                    // 将未决消息冲刷到远程节点，并且关闭该Channel
                                    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                                            .addListener(ChannelFutureListener.CLOSE);
                                }
                                
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx,
                                                            Throwable cause) {
                                    cause.printStackTrace();
                                    // 关闭Channel
                                    ctx.close();
                                }
                            });
                        }
                    });
            ChannelFuture f = bootstrap.bind().sync();
            // f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                group.shutdownGracefully().sync();
            } catch (Exception e) {
            }
        }
        
    }
    
}
