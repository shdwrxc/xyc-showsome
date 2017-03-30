package org.xyc.showsome.netty.sample;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**

 * 现在唯一缺的就是一个编码器了，和客户端的解码器对应。
 * 编码器会把UnitTime转为ByteBuf，服务端不需要添加关心分包粘包和组装
 *
 */
public class TimeEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime m = (UnixTime) msg;
        ByteBuf encoded = ctx.alloc().buffer(4);
        encoded.writeInt((int)m.value());
        //我们通过原始的ChannelPromise，当数据已经被编码后进入缓冲区后，netty可以根据此属性来标记成功还是失败
        //There is a separate handler method void flush(ChannelHandlerContext ctx) which is purposed to override the flush() operation.
        //我们不用调用flush方法，他有个独立的flush方法已经覆盖了原始的flush方法
        //更简单的例子请看TimeEncoderSimpler，和TimeDecoder一样继承ByteToMessageDecoder
        ctx.write(encoded, promise);
    }
}
