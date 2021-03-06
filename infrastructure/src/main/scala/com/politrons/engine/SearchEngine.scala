package com.politrons.engine

import com.politrons.model.{FileInfo, Rank}

import java.io.File
import java.math.{MathContext, RoundingMode}
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, ExecutionContextExecutor, Future}
import scala.io.BufferedSource
import scala.language.postfixOps
import scala.util.Try

/**
 * Factory object to read the files in the passed directory, and create an instance of [SearchEngine]
 * with the list of [FileInfo] already in memory.
 */
object SearchEngine {

  def apply(directory: String): SearchEngine = {
    val fileInfoList: List[FileInfo] = createFileInfoList(directory)
    SearchEngine(fileInfoList)
  }

  /**
   * Using the directory we load all files from the file system, and we transform each
   * in a domain model [FileInfo] which contains the name of the file, and a map with the
   * words that are part of the file
   */
  private def createFileInfoList(directory: String): List[FileInfo] = {
    getListOfFiles(directory).foldLeft(List[FileInfo]())((fileInfoList, file) => {
      println(s"Loading file ${file.getName} in search engine.")
      val words = transformSourceIntoWordsArray(scala.io.Source.fromFile(file))
      val wordsCountMap = transformWordsIntoMapWordsCount(words)
      FileInfo(file.getName, wordsCountMap) :: fileInfoList
    })
  }

  /**
   * Function to transform the BufferSource into the Array of words.
   * Once we have the array, we remove all non alphanumerics characters for each word.
   */
  private val transformSourceIntoWordsArray: BufferedSource => Array[String] = {
    source =>
      try source.mkString.split("\\s+")
        .map(text => text.replaceAll("\\W", ""))
      finally source.close()
  }

  /**
   * Function to transform the array of words into the Map of word and number of occurrences
   */
  private val transformWordsIntoMapWordsCount: Array[String] => Map[String, Int] = {
    words =>
      words.foldLeft(Map[String, Int]())((wordsCount, word) => {
        wordsCount ++ Map(word -> (wordsCount.getOrElse(word, 0) + 1))
      })
  }

  /**
   * Helper function to obtain the list of Files in a specific directory
   */
  private def getListOfFiles(dir: String): List[File] = {
    val d = new File(dir)
    if (d.exists && d.isDirectory) {
      d.listFiles.filter(_.isFile).toList
    } else {
      List[File]()
    }
  }

}

case class SearchEngine(files: List[FileInfo]) {

  implicit val ec: ExecutionContextExecutor = scala.concurrent.ExecutionContext.global

  def search(sentence: String): Try[List[(FileInfo, Rank)]] =
    Try {
      files.foldLeft(List[(FileInfo, Rank)]())((acc, fileInfo) => {
        val wordsSentence = sentence.split("\\s+")
        val wordsFounded = findWordsInFile(fileInfo, wordsSentence)
        (fileInfo, createRank(wordsFounded, wordsSentence.length)) :: acc
      }).sortWith(_._2.value > _._2.value)
    }

  /**
   * Using the current [FileInfo] with all the information we iterate over the sentence,
   * and we search each word into the map of words of that file.
   * To make the process better in terms of performance, we can remove all duplicated words in the sentence.
   * Since we assume the number of words in sentence is limited, and in order to make it more efficient,
   * we run each word of the sentence to search in a future.
   * Once we finish we gather the list of futures, and we pass into [Future.sequence] to flatten all results.
   *
   * We finally return the number of words of the sentence that are in the file.
   */
  private def findWordsInFile(fileInfo: FileInfo, wordsSentence: Array[String]): Int = {
    val futuresCount: List[Future[Int]] =
      wordsSentence
        .map(text => text.replaceAll("\\W", ""))
        .distinct
        .map(word => Future(if (fileInfo.words.contains(word)) 1 else 0))
        .toList
    Await.result(Future.sequence(futuresCount), 60 seconds).sum
  }

  /**
   * Algorithm to create the Rank with the specific percentage of words found in the file.
   * Since the number of words in the map are distinct, just like the ones in the sentence,
   * we can ensure the calc making this [rule of three]
   */
  private def createRank(wordsFounded: Int, sentenceLength: Int): Rank = {
    val totalRank: BigDecimal = (BigDecimal(wordsFounded * 100) / BigDecimal(sentenceLength))
      .round(new MathContext(4, RoundingMode.HALF_EVEN))
    Rank(if (totalRank > 100) 100 else totalRank)
  }
}
