package org.example.myhome.test

import java.io.PrintWriter
import java.net.Socket



fun main(args: Array<String>) {
  println("Hello")
  val echoSocket = Socket("localhost", 8080)
  val out = PrintWriter(echoSocket.outputStream, true)
  out.write("Hello world")
  Thread.sleep(300)
  out.close()
  Thread.sleep(2000)
  echoSocket.close()
  println("End")
}
