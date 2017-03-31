package org.xyc.showsome.sample.netty;

import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 * ByteToMessageDecoder是ChannelInboundHandler的一个实现。为了让处理分包和粘包的变得更加容易
 */
public class TimeDecoder extends ByteToMessageDecoder {

    /**
     * ByteToMessageDecoder调用decode方法在缓冲区中累计数据，当新数据来的时候
     * @param ctx
     * @param in
     * @param out
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        //decode方法可以决定，在缓冲区数据没满时，不往out里发数据，而是继续等待新数据
        //当新数据到达时，继续这个循环
        if (in.readableBytes() < 4) {
            return;
        }
        //当decode往out里发送数据时，意味着接受数据已经成功了，ByteToMessageDecoder会把缓冲区里已经读到的数据丢弃，
        //你不需要对条消息进行decode，ByteToMessageDecoder会持续的调用decode方法直到没有
        out.add(in.readBytes(4));
    }
}
