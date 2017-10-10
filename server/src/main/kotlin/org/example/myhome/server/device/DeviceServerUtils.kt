package org.example.myhome.server.device

import io.netty.handler.codec.LengthFieldBasedFrameDecoder

const val INITIAL_FRAME_LIMIT = 256
const val LENGTH_DECODER = "lengthDecoder"

fun createLengthDecoder(frameLimit: Int) = LengthFieldBasedFrameDecoder(frameLimit, 0, 4, 0, 4)
