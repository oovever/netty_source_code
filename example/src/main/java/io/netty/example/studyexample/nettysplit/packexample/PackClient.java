package io.netty.example.studyexample.nettysplit.packexample;


import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class PackClient {

    EventLoopGroup eventLoopGroup = new NioEventLoopGroup();

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap
                    .group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(
                            new ChannelInitializer<SocketChannel>() {
                                @Override
                                protected void initChannel(
                                        SocketChannel socketChannel
                                ) {
                                    socketChannel.pipeline().addLast(
                                            new NettyClientHandler()
                                    );
                                }
                            }
                    );

            System.out.println("client is ready...");
            ChannelFuture channelFuture = bootstrap.connect(
                    "127.0.0.1",
                    8080).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
