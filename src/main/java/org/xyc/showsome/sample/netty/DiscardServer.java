package org.xyc.showsome.sample.netty;

import io.netty.bootstrap.ServerBootstrap;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Discards any incoming data.
 */
public class DiscardServer {

    private int port;

    public DiscardServer(int port) {
        this.port = port;
    }

    public void run() throws Exception {
        //NioEventLoopGroup是多线程的循环事件处理器，用于处理io操作
        //netty提供不同的EventLoopGroup实现类，用于不同场景的传输，我们实现了服务端的应用，会实现两个NioEventLoopGroup
        //第一个叫boss，接受请求的连接
        //第二个叫worker，boss只要接受了情况，并且把这个请求注册给worker后，worker就会执行请求的操作
        //多少个线程被使用，以及这些线程怎么分配到channel上，区别去EventLoopGroup的实现，通过很多方式，比如构造方法
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //ServerBootstrap是一个帮助类，可以用于启动改服务器。
            //也可以直接用一个通道来启动，但是这种方式不太推荐，大部分情况下不需要这么做
            //ServerBootstrap is a helper class that sets up a server. You can set up the server using a Channel directly. However, please note that this is a tedious process, and you do not need to do that in most cases.
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    //使用NioServerSocketChannel类，他可以用来新建一个channel来接受请求，我们一次来举例说明如果接受一个新的请求
                    .channel(NioServerSocketChannel.class)
                    //被指定的handler会一直被新接受的channel使用
                    //ChannelInitializer是一个特殊的handler，帮助用来来配置一个新的channel
                    //就像你要给新的channel的配置ChannelPipeline，比如新加几个handler，比如DiscardServerHandler ，来实现我们的网络应用。
                    //慢慢我们的网络应用越来越复杂，可能需要加越来越多的handler到pipeline，最后选取这个匿名类作为最高一级的类
                    //总结下，ChannelInitializer主要还是为了新加一个handler比较方便点，可以直接写内部类
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new DiscardServerHandler());
                        }
                    })
                    //可以给channel的实现设定参数，我们使用tcp，就可以指定socket的选项，比如tcpNoDelay 或者 keepAlive等
                    //请参考ChannelOption的doc和特定的ChannelConfig的实现类，来对支持的ChannelOptions有个了解
                    .option(ChannelOption.SO_BACKLOG, 128)
                    //option()是设置给NioServerSocketChannel接受请求的，childOption() is for the Channels accepted by the parent ServerChannel, which is NioServerSocketChannel in this case.
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        new DiscardServer(port).run();
    }
}