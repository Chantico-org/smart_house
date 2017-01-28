package org.example.myhome.utils

import io.netty.channel.Channel
import io.netty.channel.ChannelFuture

fun ChannelFuture.syncChannel(): Channel? {
  return this.sync()?.channel()
}
