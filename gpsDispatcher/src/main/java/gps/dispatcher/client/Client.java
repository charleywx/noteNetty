package gps.dispatcher.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.InetSocketAddress;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 客户端
 */
@Component
public class Client {
    
    public final Logger log = LoggerFactory.getLogger(this.getClass());
    
    @Autowired
    private ConcurrentLinkedQueue queue;
    
    private String host = "localhost";
    private int port = 18180;
    
    public static void main(String[] args) throws Exception {
        new Client().start();
    }
    
    public void start() {
        NioEventLoopGroup group = new NioEventLoopGroup();
        try {
            // 引导类
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 添加Client
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new SimpleChannelInboundHandler<ByteBuf>() {
                                
                                // 在到服务器的连接已经建立之后将被调用
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) {
                                    ctx.writeAndFlush(Unpooled.copiedBuffer("客户端：连接已建立", CharsetUtil.UTF_8));
                                }
                                
                                // 当从服务器接受一条消息时被调用
                                @Override
                                public void channelRead0(ChannelHandlerContext ctx, ByteBuf in) {
//                                    queue.add(in.toString(CharsetUtil.UTF_8));
                                //        log.info("客户端收到消息: " + in.toString(CharsetUtil.UTF_8));
                                }
                                
                                // 在处理过程中发生异常时被调用
                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
                                    cause.printStackTrace();
                                    ctx.close();
                                }
                                
                            });
                        }
                    });
            // 连接远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            /*// 阻塞直到Channel关闭
            f.channel().closeFuture().sync();*/
        }catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                // 关闭连接池并且释放所有资源
                group.shutdownGracefully().sync();
            } catch (Exception e) {
            }
        }
    }
}

