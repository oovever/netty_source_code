/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package io.netty.example.echo;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.nio.NioEventLoop;
import io.netty.util.ReferenceCounted;
import java.net.InetSocketAddress;

/**
 * Handler implementation for the echo server.
 */
@Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ((NioEventLoop) ctx.channel().eventLoop()).executeAfterEventLoopIteration(new Runnable() {
            @Override public void run() {
                System.out.println("read...." + msg);
            }
        });
        Thread.sleep(1000L);
//        ctx.fireChannelRead(msg);
        ctx.write(msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ((NioEventLoop) ctx.channel().eventLoop()).executeAfterEventLoopIteration(new Runnable() {
            @Override public void run() {
                System.out.println("read complete....");
            }
        });
        System.out.println("read complete before executeAfterEventLoopIteration");
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        ((NioEventLoop) ctx.channel().eventLoop()).executeAfterEventLoopIteration(new Runnable() {
            @Override public void run() {
                System.out.println("read error....");
            }
        });
        cause.printStackTrace();
        ctx.close();
    }
}