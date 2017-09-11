package com.quambo.utils

import java.io.{File, FileWriter}

object io {
  def printToFile(f: String, content: Array[String]): Unit = {
    val fw = new FileWriter(f, true)
    val msg = content.mkString(", ")
    try { fw.write(msg) } finally { fw.close() }
  }

  def log(logInput: Array[String]): Unit = {
    printToFile("log.txt", logInput :+ "\n")
  }

  def createLogFile(): Unit ={
    new File("log.txt")
  }
}
