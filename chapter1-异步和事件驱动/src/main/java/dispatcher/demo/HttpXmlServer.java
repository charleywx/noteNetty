package dispatcher.demo;

import java.net.InetSocketAddress;
 
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.AdaptiveRecvByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
 
public class HttpXmlServer implements Runnable {
 
    private EventLoopGroup bossGroup = null;
    private EventLoopGroup workerGroup = null;
    private SimpleChannelInboundHandler<HttpXmlRequest> handler = null;
 
    private int port = 9999;
 
    @SuppressWarnings("unused")
    private HttpXmlServer() {
    }
 
    public HttpXmlServer(int _port, SimpleChannelInboundHandler<HttpXmlRequest> _handler) {
        this.port = _port;
        this.handler = _handler;
    }
 
    @Override
    public void run() {
        // 处理网络连接---接受请求
        bossGroup = new NioEventLoopGroup();
        // 进行socketChannel的网络读写
        workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                    // boss线程接收参数设置，BACKLOG用于构造服务端套接字ServerSocket对象，
                    // 标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。
                    // 如果未设置或所设置的值小于1，Java将使用默认值50。
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    // 发送缓冲器
                    .option(ChannelOption.SO_SNDBUF, 1024)
                    // 接收缓冲器
                    .option(ChannelOption.SO_RCVBUF, 1024)
                    // 接收缓冲分配器
                    .option(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(256, 2048, 65536))
                    // work线程参数设置
                    .childOption(ChannelOption.RCVBUF_ALLOCATOR, new AdaptiveRecvByteBufAllocator(256, 2048, 65536))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
                            ch.pipeline().addLast("http-aggregator", new HttpObjectAggregator(65536));
                            ch.pipeline().addLast("xml-decoder",
                                    new HttpXmlRequestDecoder(HttpRequestMessage.class, true));
                            ch.pipeline().addLast("http-encoder", new HttpResponseEncoder());
                            ch.pipeline().addLast("xml-encoder", new HttpXmlResponseEncoder());
                            ch.pipeline().addLast("xmlServerHandler", handler);
                        }
                    });
            ChannelFuture future = b.bind(new InetSocketAddress(port)).sync();
            System.out.println("HTTP netty server started. the port is " + port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
 
    public void shutdown() {
        if (bossGroup != null)
            bossGroup.shutdownGracefully();
        if (workerGroup != null)
            workerGroup.shutdownGracefully();
    }
}