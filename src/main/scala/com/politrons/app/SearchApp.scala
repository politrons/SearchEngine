package com.politrons.app

import com.politrons.engine.SearchEngine
import com.politrons.model.FileInfo

import scala.annotation.tailrec
import scala.io.StdIn.readLine

object SearchApp {


  def main(args: Array[String]) {
    val directory = args(0)
    searchInput(SearchEngine(directory))
  }

  @tailrec
  private def searchInput(searchEngine: SearchEngine): Unit = {
    println("========================================")
    print("Search>")
    val line = readLine()
    println("Searching......: " + line)
    val infoFilesRank = searchEngine.search(line)
    printOutput(infoFilesRank)
    searchInput(searchEngine)
  }

  private def printOutput(infoFilesRank: List[(FileInfo, SearchEngine.Rank)]): Unit = {
    infoFilesRank.foreach(infoFileRank => {
      println(s"${infoFileRank._1.name}:${infoFileRank._2.value}%")
    })
  }
}
