package com.politrons.app

import com.politrons.engine.SearchEngine
import com.politrons.service.SearchService

import scala.annotation.tailrec
import scala.io.StdIn.readLine

object SearchApp {


  def main(args: Array[String]) {
    println(
      """
        | _____                     _
        |  / ____|                   | |
        | | (___   ___  __ _ _ __ ___| |__     __ _ _ __  _ __
        |  \___ \ / _ \/ _` | '__/ __| '_ \   / _` | '_ \| '_ \
        |  ____) |  __/ (_| | | | (__| | | | | (_| | |_) | |_) |
        | |_____/ \___|\__,_|_|  \___|_| |_|  \__,_| .__/| .__/
        |                                          | |   | |
        |                                          |_|   |_|
        |""".stripMargin)
    val directory = args(0)
    println(s"Loading files in folder ${directory}")
    val searchEngine: SearchEngine = SearchEngine(directory)
    val service = SearchService(searchEngine)
    getInputSentence(service)
  }

  @tailrec
  private def getInputSentence(service: SearchService): Unit = {
    println("========================================")
    print("Search>")
    val output = service.searchInput(readLine())
    println(
      """
        | +-+-+-+-+-+ +-+-+-+-+
        | |T|o|t|a|l| |R|a|n|k|
        | +-+-+-+-+-+ +-+-+-+-+
        |""".stripMargin)
    println(output.toString)
    getInputSentence(service)
  }


}
