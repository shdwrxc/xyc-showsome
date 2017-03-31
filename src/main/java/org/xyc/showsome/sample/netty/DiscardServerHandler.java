package org.xyc.showsome.sample.netty;

import io.netty.buffer.ByteBuf;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * Handles a server-side channel.
 *
 * user guide的翻译
 * http://ifeve.com/netty5-user-guide/
 * or
 * http://wiki.jikexueyuan.com/project/netty-4-user-guide/writing-discard-server.html
 *
 * ChannelInboundHandlerAdapter已经实现了一系列的操作，可以复用，也可以重写。省去了实现接口时的各种方法实现
 *
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 数据到达时会触发
     * @param ctx
     * @param msg
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        //ByteBuf是一个reference-counted的实例，必须通过显示的release方法释放
        //请注意，在handler里应该始终要释放掉任何reference-counted类型的实例。
        //channelRead方法通常是这样的
//        @Override
//        public void channelRead(ChannelHandlerContext ctx, Object msg) {
//            try {
//                 Do something with msg
//            } finally {
//                ReferenceCountUtil.release(msg);
//            }
//        }
        // Discard the received data silently.
//        ((ByteBuf) msg).release();
        ByteBuf in = (ByteBuf) msg;
        try {
            //This inefficient loop can actually be simplified to: System.out.println(in.toString(io.netty.util.CharsetUtil.US_ASCII))
            while (in.isReadable()) { // (1)
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            //Alternatively, you could do in.release() here
            ReferenceCountUtil.release(msg);
        }

        //下面的写法可以发送消息给客户端
        //不同于上面，我们在发送消息后，netty可以自动关闭资源，无需自己动手关闭
//        ctx.write(msg); // (1)
        //write方法会把数据写到缓冲期，需要在调用flush方法会发送出去，或者可以直接使用ctx.writeAndFlush(msg)的简单方法。
//        ctx.flush(); // (2)
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        //netty发生io错误时，或handler发生异常时，会调用这个方法
        //在发生了异常后，通常应该记录下，做些你自己的处理，然后应该关掉这个channel
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
