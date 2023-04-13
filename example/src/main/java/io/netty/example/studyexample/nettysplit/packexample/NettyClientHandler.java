package io.netty.example.studyexample.nettysplit.packexample;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class NettyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    // 记录 read 事件处理次数
    private int count;

    @Override
    public void channelActive(ChannelHandlerContext ctx)
            throws Exception {
        // 向服务器发送多个短文本，使用 for 循环发送 10 次
        for (int i = 0; i < 10; i++) {
            ctx.writeAndFlush(
                    Unpooled.copiedBuffer(
                            "\nhello server, " + i,
                            CharsetUtil.UTF_8
                    )
            );
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);

        // 将 buffer 转成字符串
        java.lang.String message = new java.lang.String(buffer, CharsetUtil.UTF_8);
        System.out.println("data received: " + message);
        System.out.println("data reception time: " + (++count));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        // 关闭与客户端的 Socket 连接
        cause.printStackTrace();
        ctx.channel().close();
    }
}
