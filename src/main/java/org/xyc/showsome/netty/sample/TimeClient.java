package org.xyc.showsome.netty.sample;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
    public static void main(String[] args) throws Exception {
        String host = args[0];
        int port = Integer.parseInt(args[1]);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //Bootstrap和ServerBootstrap比较类似，但他是服务于客户端的，没有连接channel等
            Bootstrap b = new Bootstrap();
            //如果你只指定了一个EventLoopGroup，他会既被用作boss，也被用作worker。虽然客户端不需要boss。。。
            b.group(workerGroup);
            //不同于NioServerSocketChannel, NioSocketChannel创建客户端channel
            b.channel(NioSocketChannel.class);
            //注意下，这里没有childOption()，不想ServerBootstrap ，因为客户端的SocketChannel 没有父channel
            b.option(ChannelOption.SO_KEEPALIVE, true);
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
//                    ch.pipeline().addLast(new TimeClientHandler());
                    ch.pipeline().addLast(new TimeDecoder(), new TimeClientHandler());
                }
            });

            // Start the client.
            //客户端使用connect方法
            ChannelFuture f = b.connect(host, port).sync();

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }
}
