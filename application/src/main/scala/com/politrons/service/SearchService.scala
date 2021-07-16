package com.politrons.service

import com.politrons.engine.SearchEngine
import com.politrons.model.{FileInfo, Rank}

import scala.util.{Failure, Success}

case class SearchService(searchEngine: SearchEngine) {

  /**
   * We receive the sentence to be passed to the search engine, and then once we
   * receive the response we filter the number of elements to be send back to the app.
   *
   * @param sentence to be used in the [SearchEngine] to find the files with more ocurrences.
   * @return StringBuffer with the output to be send back to the client.
   */
  def searchInput(sentence: String): StringBuffer = {
    println("Searching......: " + sentence)
    searchEngine.search(sentence) match {
      case Success(infoFilesRank) => generateOutput(infoFilesRank)
      case Failure(exception) =>
        println(s"Error in Search engine. Caused by ${exception}")
        new StringBuffer(s"Internal error. Caused by ${exception.getMessage}")
    }
  }

  /**
   * We transform the List[(FileInfo, Rank)] into the [StringBuffer] to be render by the app
   * As functional requirement we only take the first ten files of the rank.
   *
   * @param infoFilesRank list of tuple FileInfo with the info of the file, and Rank with the
   *                      percentage of occurrence of the sentence in the files
   * @return
   */
  private def generateOutput(infoFilesRank: List[(FileInfo, Rank)]): StringBuffer = {
    infoFilesRank
      .take(10)
      .foldLeft(new StringBuffer())((output, infoFileRank) => {
        if (infoFileRank._2.value > 0) {
          output.append(s"${infoFileRank._1.name}:${infoFileRank._2.value}%\n")
        } else {
          output
        }
      })
      .ifEmptyAddDefault()
  }

  implicit class BufferExtension(buffer: StringBuffer) {
    def ifEmptyAddDefault(): StringBuffer = {
      if (buffer.length() == 0) {
        buffer.append("no matches found")
      } else {
        buffer
      }
    }
  }
}
