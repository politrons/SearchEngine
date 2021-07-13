package com.politrons.app

import com.politrons.engine.SearchEngine
import com.politrons.model.{FileInfo, Rank}

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
    searchInput(SearchEngine(directory))
  }

  @tailrec
  private def searchInput(searchEngine: SearchEngine): Unit = {
    println("========================================")
    print("Search>")
    val line = readLine()
    println("Searching......: " + line)
    val infoFilesRank = searchEngine.search(line)
    println(
      """
        | +-+-+-+-+-+ +-+-+-+-+
        | |T|o|t|a|l| |R|a|n|k|
        | +-+-+-+-+-+ +-+-+-+-+
        |""".stripMargin)
    printOutput(infoFilesRank)
    searchInput(searchEngine)
  }

  private def printOutput(infoFilesRank: List[(FileInfo, Rank)]): Unit = {
    infoFilesRank.foreach(infoFileRank => {
      println(s"${infoFileRank._1.name}:${infoFileRank._2.value}%")
    })
  }
}
