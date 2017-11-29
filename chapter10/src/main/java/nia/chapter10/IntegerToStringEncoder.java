package nia.chapter10;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageEncoder;

import java.util.List;

/**
 * Listing 10.6 Class IntegerToStringEncoder
 */
public class IntegerToStringEncoder
        extends MessageToMessageEncoder<Integer> {
    @Override
    public void encode(ChannelHandlerContext ctx, Integer msg,
                       List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}

