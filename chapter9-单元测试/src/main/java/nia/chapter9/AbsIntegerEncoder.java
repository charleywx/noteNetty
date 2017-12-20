package nia.chapter9;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * 测试出站消息
 * Listing 9.3 AbsIntegerEncoder
 */
public class AbsIntegerEncoder extends
        MessageToMessageEncoder<ByteBuf> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf in, List<Object> out) throws Exception {
        while (in.readableBytes() >= 4) { // 检查是否由足够的字节来编码
            int value = Math.abs(in.readInt()); // 从输入的ByteBuf读取下一个整数，并且计算绝对值
            out.add(value); // 写入消息list
        }
    }
}
