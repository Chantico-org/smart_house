package org.example.myhome.models

import io.netty.channel.Channel

class Device(
  val channel: Channel
) : Channel by channel
