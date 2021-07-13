package com.politrons.engine

import com.politrons.engine.SearchEngine.Rank
import com.politrons.model.FileInfo

import java.io.File
import scala.::
import scala.io.BufferedSource

/**
 * Factory object to read the files in the passed directory, and create an instance of [SearchEngine]
 * with the list of [FileInfo] already in memory.
 */
object SearchEngine {

  def apply(directory: String): SearchEngine = {

    val fileInfoList = getListOfFiles(directory).foldLeft(List[FileInfo]())((fileInfoList, file) => {
      val words = transformSourceIntoWordsArray(scala.io.Source.fromFile(file))
      FileInfo(file.getName, createWordsCount(words)) :: fileInfoList
    })

    SearchEngine(fileInfoList)
  }

  private val transformSourceIntoWordsArray: BufferedSource => Array[String] = {
    source =>
      try source.mkString.split("\\s+")
        .map(text => text.replaceAll("\\W", ""))
      finally source.close()
  }

  private def createWordsCount(words: Array[String]): Map[String, Int] = {
    words.foldLeft(Map[String, Int]())((wordsCount, word) => {
      wordsCount ++ Map(word -> (wordsCount.getOrElse(word, 1) + 1))
    })
  }

  private def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

  case class Rank(value: Int) extends AnyVal
}

case class SearchEngine(files: List[FileInfo]) {

  def search(sentence: String): List[(FileInfo, Rank)] = {
    files.foldLeft(List[(FileInfo, Rank)]())((acc, fileInfo) => {
      val wordsFounded = findWordsInFile(fileInfo, sentence)
      val totalWords = fileInfo.words.values.size
      (fileInfo, createRank(wordsFounded, totalWords)) :: acc
    }).sortWith(_._2.value > _._2.value)
  }

  /**
   * Using the current [FileInfo] with all the information
   * @param fileInfo
   * @param sentence
   * @return
   */
  private def findWordsInFile(fileInfo: FileInfo, sentence: String): Int = {
    sentence.split("\\s+")
      .map(text => text.replaceAll("\\W", ""))
      .distinct
      .count(word => fileInfo.words.contains(word))
  }

  private def createRank(wordsFounded: Int, totalWords: Int): Rank = {
    val totalRank = wordsFounded * 100 / totalWords
    Rank(if (totalRank > 100) 100 else totalRank)
  }
}
