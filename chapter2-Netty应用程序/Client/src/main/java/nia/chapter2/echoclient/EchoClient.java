package nia.chapter2.echoclient;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;

/**
 * Listing 2.4 Main class for the client
 */
public class EchoClient {
    private final String host;
    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static void main(String[] args) throws Exception {
//        if (args.length != 2) {
//            System.err.println("Usage: " + EchoClient.class.getSimpleName() +
//                    " <host> <port>"
//            );
//            return;
//        }
//
//        final String host = args[0];
//        final int port = Integer.parseInt(args[1]);
//        new EchoClient(host, port).start();

        new EchoClient("127.0.0.1", 18181).start();
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .remoteAddress(new InetSocketAddress(host, port))
                    // 添加Client
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ch.pipeline().addLast(
                                    new EchoClientHandler());
                        }
                    });
            // 连接远程节点，阻塞等待直到连接完成
            ChannelFuture f = b.connect().sync();
            // 阻塞直到Channel关闭
            f.channel().closeFuture().sync();
        } finally {
            // 关闭连接池并且释放所有资源
            group.shutdownGracefully().sync();
        }
    }
}

