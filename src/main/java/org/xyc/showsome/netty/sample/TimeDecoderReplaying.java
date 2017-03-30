package org.xyc.showsome.netty.sample;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

/**
 * 你可以希望使用ReplayingDecoder让解码更简单，请好好参考说明文档
 *
 * 另外netty提供非常多的使用的解码类，请参考
 *
 * io.netty.example.factorial，字节流
 * http://netty.io/4.0/xref/io/netty/example/factorial/package-summary.html
 *
 * io.netty.example.telnet，文本流
 * http://netty.io/4.0/xref/io/netty/example/telnet/package-summary.html
 */
public class TimeDecoderReplaying extends ReplayingDecoder<Void> {
    @Override
    protected void decode(
            ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(in.readBytes(4));
    }
}
