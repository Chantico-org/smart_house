package org.example.myhome.test

import java.io.InputStream
import java.net.ServerSocket
import java.net.Socket

fun main(args: Array<String>) {
  val serverSocket:ServerSocket = ServerSocket(8080)
  while (true) {
    println("Waiting for client")
    val client: Socket = serverSocket.accept()
    println("New connection")
    val input: InputStream = client.inputStream
    do {
      val char = input.read()
      print("$char ")
    }while (char !== -1)
    println()
    println("Connection end")
  }
}
