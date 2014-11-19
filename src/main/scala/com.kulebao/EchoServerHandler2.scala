package com.kulebao

import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}

class EchoServerHandler2 extends ChannelInboundHandlerAdapter {
  // (1)
  override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef) ={
    ctx.write(msg)
  }

  override def channelReadComplete(ctx: ChannelHandlerContext) = {
    ctx.flush
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = {
    cause.printStackTrace
    ctx.close
  }
}