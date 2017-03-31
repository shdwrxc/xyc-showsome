package org.xyc.showsome.sample.netty;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 本类中的方法不适合用在项目中，请参考TimeDecoder
 */
public class TimeClientHandler1 extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;

    /**
     * ChannelHandler有2个生命周期监听方法，handlerAdded和handlerRemoved。
     * 你可以做些操作，只要不被堵塞很多时间
     * 这两个方法在这个例子里做了一件事情，把数据固定放4个字节，然后读的时候只读4个字节，保证避免出现分包和粘包问题
     * @param ctx
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buf = ctx.alloc().buffer(4);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buf.release();
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        //接受到的数据累积在buf里
        buf.writeBytes(m);
        m.release();

        //检查长度是否达到了4，到了4才读取，如果不满4，会重复调用channelRead方法，累积数据
        if (buf.readableBytes() >= 4) {
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
