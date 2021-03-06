package com.kulebao

import java.nio.charset.Charset

import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled._
import io.netty.channel.{ChannelHandlerContext, ChannelInboundHandlerAdapter}
import org.joda.time.DateTime
import org.slf4j.LoggerFactory

class SimpleServerHandler extends ChannelInboundHandlerAdapter {
  val logger = LoggerFactory.getLogger("SimpleServerHandler")

  override def channelRead(ctx: ChannelHandlerContext, msg: AnyRef) = {

    val buf = ctx.alloc().buffer()
    val local: ByteBuf = msg.asInstanceOf[ByteBuf]

    val input: String = local.toString(Charset.defaultCharset())
    logger.debug(s"receive: $input")
    local.release()

    val output: String = reply(input)

    logger.debug(s"reply: $output")
    val byteBuffer = copiedBuffer(output, Charset.defaultCharset())
    buf.writeBytes(byteBuffer)

    ctx.write(buf)
  }

  def reply(input: String): String = {
    val prefix = input.split(",").take(2).mkString(",")

    val time: DateTime = DateTime.now()
    val formattedTime = time.toString("HHmmss")

    s"$prefix,D1,$formattedTime,5,1#"
  }

  override def channelReadComplete(ctx: ChannelHandlerContext) = {
    ctx.flush
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) = {
    logger.error(cause.getLocalizedMessage)
    ctx.close
  }
}