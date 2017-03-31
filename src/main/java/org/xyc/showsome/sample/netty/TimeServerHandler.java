package org.xyc.showsome.sample.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 方法将会在连接被建立并且准备进行通信时被调用。
     * @param ctx
     */
    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
        //我们需要新建一个buffer来装消息，我们要写32位的整形，就需要4个字节，同时用这种方式来构建一个ByteBuf
        final ByteBuf time = ctx.alloc().buffer(4);
        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));

        //这样就可以发消息了。
        //但是flip方法在哪里，nio里，发送消息不是应该调用java.nio.ByteBuffer.flip()方法吗？
        //ByteBuf没有这个方法因为他有2个指针。一个指向读操作，一个指向写操作，写是写的指针会向前走，但是读的指针不会变
        //读指针和写指针分别代表了开始和结束
        //NIO buffer没有提供一个简单的方法来分辨开始和结束，如果没有调用flip方法的话。你会遇到麻烦如果忘记调用了flip方法，错误的数据会被发送
        //这类错误不会在netty中发生，因为netty给不同类型的操作提供了不同的指针
        //另外还有一点，ChannelHandlerContext.write()和writeAndFlush()方法返回一个ChannelFuture方法，ChannelFuture代表了一个还未发生的io操作
        //这意味这任何请求都可能还未被执行，因为所有的操作在netty里都是异步的
        //下面的代码可能在发送消息前就关闭了连接
//        Channel ch = ...;
//        ch.writeAndFlush(message);
//        ch.close();
        //所以你需要在write返回的ChannelFuture结束后才能调用close方法，ChannelFuture在完成后会通知他的监听者
        //close方法也会返回一个ChannelFuture，他也不是马上就去执行的
        //那怎么获得ChannelFuture执行完成后的通知呢？给ChannelFuture添加ChannelFutureListener。这里我们添加了一个匿名的监听器，在执行完成后关闭连接
        //或者简单的添加一个已经预定义好的监听器f.addListener(ChannelFutureListener.CLOSE);
        final ChannelFuture f = ctx.writeAndFlush(time);
//        ChannelFuture f = ctx.writeAndFlush(new UnixTime());  //can use pojo
        f.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) {
                assert f == future;
                ctx.close();
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
