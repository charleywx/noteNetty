package nia.chapter11;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;

import javax.net.ssl.SSLEngine;

/**
 * Listing 11.1 Adding SSL/TLS support
 */
public class SslChannelInitializer extends ChannelInitializer<Channel> {
    private final SslContext context;
    private final boolean startTls;

    /**
     * 构造方法
     * @param context
     * @param startTls  如果设置为True,第一个写入的消息将不会被加密，一般客户端应该设置为True
     */
    public SslChannelInitializer(SslContext context,
                                 boolean startTls) {
        this.context = context;
        this.startTls = startTls;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        // 对于每一个SslHandler实例，都使用Channel的ByteBufAllocator从SslContext获取一个新的SSLEngine
        SSLEngine engine = context.newEngine(ch.alloc());
        // 作为第一个ChannelHandler添加到ChannelPipeline中
        ch.pipeline().addFirst("ssl",
                new SslHandler(engine, startTls));
    }
}
