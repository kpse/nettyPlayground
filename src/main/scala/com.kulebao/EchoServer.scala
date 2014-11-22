package com.kulebao

import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.socket.SocketChannel
import io.netty.channel._
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel


object Server {
  val PORT = System.getProperty("port", "7755").toInt

  def main(args: Array[String]): Unit = {
    val bossGroup: EventLoopGroup = new NioEventLoopGroup(1)
    val workerGroup: EventLoopGroup = new NioEventLoopGroup
    try {
      val b: ServerBootstrap = new ServerBootstrap
      b.group(bossGroup, workerGroup).channel(classOf[NioServerSocketChannel])
        .option[Integer](ChannelOption.SO_BACKLOG, 100).childHandler(new ChannelInitializer[SocketChannel] {
        def initChannel(ch: SocketChannel) {
          val p: ChannelPipeline = ch.pipeline
          p.addLast(new SimpleServerHandler)
        }
      })


      val f: ChannelFuture = b.bind(PORT).sync
      f.channel.closeFuture.sync
    } finally {
      bossGroup.shutdownGracefully
      workerGroup.shutdownGracefully
    }
  }

}
