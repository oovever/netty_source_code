package io.netty.example.studyexample.nettysplit.packexample;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;
import java.util.UUID;

public class NettyServerHandler extends SimpleChannelInboundHandler<ByteBuf> {

    // 记录 read 事件处理次数
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        // 将 buffer 转成字符串
        String message = new String(buffer, CharsetUtil.UTF_8);
        System.out.println("data received: " + message);
        System.out.println("data reception time: " + (++count));

        // 服务器回送一个 UUID 给客户端
        ctx.writeAndFlush(
                Unpooled.copiedBuffer(
                        "\n" + UUID.randomUUID().toString(),
                        CharsetUtil.UTF_8
                )
        );
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // 关闭与服务器端的 Socket 连接
        cause.printStackTrace();
        ctx.channel().close();
    }
}
