package nia.chapter10;

import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * 编解码器
 * 联合通道双重处理器
 * Listing 10.10 CombinedChannelDuplexHandler<I,O>
 */
public class CombinedByteCharCodec extends
        CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {
    public CombinedByteCharCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
