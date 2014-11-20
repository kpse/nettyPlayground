package com.kulebao

import java.nio.charset.Charset

import io.netty.buffer.Unpooled._
import java.util.Date

import io.netty.buffer.ByteBuf
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import org.slf4j.LoggerFactory

class SimpleServerHandler extends ChannelInboundHandlerAdapter {

  override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef) = {
    val logger = LoggerFactory.getLogger("SimpleServerHandler")
    val buf = ctx.alloc().buffer()
    val local: ByteBuf = msg.asInstanceOf[ByteBuf]
    val currentTimeMillis = System.currentTimeMillis()
    val input: String = local.toString(Charset.defaultCharset())
    logger.debug(input)
    val output: String = String.format("You just input: %s at %s \n", input, new Date(currentTimeMillis).toString)
    logger.debug(output)
    val byteBuffer = copiedBuffer(output, Charset.defaultCharset())
    buf.writeBytes(byteBuffer)
    local.release()
    ctx.write(buf)
  }

  override def channelReadComplete(ctx: ChannelHandlerContext) = {
    ctx.flush
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = {
    cause.printStackTrace
    ctx.close
  }
}