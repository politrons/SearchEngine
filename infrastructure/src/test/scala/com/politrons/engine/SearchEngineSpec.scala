package com.politrons.engine

import org.scalatest.{BeforeAndAfterAll, FeatureSpec, GivenWhenThen}

import java.nio.file.{Path, Paths}

class SearchEngineSpec extends FeatureSpec with GivenWhenThen with BeforeAndAfterAll {

  feature(" Search engine ") {

    scenario("All files properly loaded") {

      Given("a folder path where we have the files")
      val resourcesPath = getClass.getResource("/files")
      val filesPath = resourcesPath.getPath
      When("I instantiate the SearchEngine")
      val engine = SearchEngine(filesPath)
      Then("I FileInfo list is loaded in memory filter and properly")
      val files = engine.files
      assert(files.nonEmpty)
      assert(files.size == 4)

      assert(files.last.name == "hello_world.txt")
      assert(files.last.words.size == 2)
      assert(files.last.words.contains("hello"))
      assert(files.last.words.contains("world"))
      assert(!files.last.words.contains(","))
      assert(files.last.words("hello") == 2)
      assert(files.last.words("world") == 1)

      assert(files.head.words.size == 23)
      assert(files.head.words("make") == 3)

    }

    scenario("100% 50% and 0% rank files with 'hello world' sentence ") {

      Given("a search engine with files loaded")
      val resourcesPath = getClass.getResource("/files")
      val filesPath = resourcesPath.getPath
      When("I search for hello world sentence")
      val engine = SearchEngine(filesPath)
      Then("I receive the 100% rank top")
      val infoFilesRank = engine.search("hello world")
      assert(infoFilesRank.size == 4)
      assert(infoFilesRank.head._2.value == 100)
      assert(infoFilesRank(1)._2.value == 50)
      assert(infoFilesRank(2)._2.value == 0)
      assert(infoFilesRank(3)._2.value == 0)
    }
    scenario("50% 50% and 0% rank files with 'hello' sentence ") {

      Given("a search engine with files loaded")
      val resourcesPath = getClass.getResource("/files")
      val filesPath = resourcesPath.getPath
      When("I search for hello world sentence")
      val engine = SearchEngine(filesPath)
      Then("I receive the 50% rank top")
      val infoFilesRank = engine.search("hello")
      assert(infoFilesRank.size == 4)
      assert(infoFilesRank.head._2.value == 50)
      assert(infoFilesRank(1)._2.value == 50)
      assert(infoFilesRank(2)._2.value == 0)
      assert(infoFilesRank(3)._2.value == 0)
    }

    scenario("float point 0%  0% and 0% rank file with 'Quijote' sentence ") {

      Given("a search engine with files loaded")
      val resourcesPath = getClass.getResource("/files")
      val filesPath = resourcesPath.getPath
      When("I search for hello world sentence")
      val engine = SearchEngine(filesPath)
      Then("I receive the 50% rank top")
      val infoFilesRank = engine.search("Muchas y muy graves historias he yo leído de caballeros andantes")
      assert(infoFilesRank.size == 4)
      assert(infoFilesRank.head._2.value == 0.07340)
      assert(infoFilesRank(1)._2.value == 0)
      assert(infoFilesRank(2)._2.value == 0)
      assert(infoFilesRank(3)._2.value == 0)
    }

    scenario("100% 50% float point and 0% rank files with 'Quijote' and 'hello world' sentence ") {

      Given("a search engine with files loaded")
      val resourcesPath = getClass.getResource("/files")
      val filesPath = resourcesPath.getPath
      When("I search for hello world sentence")
      val engine = SearchEngine(filesPath)
      Then("I receive the 50% rank top")
      val infoFilesRank = engine.search("hello world Muchas y muy graves historias he yo leído de caballeros andantes")
      assert(infoFilesRank.size == 4)
      assert(infoFilesRank.head._2.value == 100)
      assert(infoFilesRank(1)._2.value == 50)
      assert(infoFilesRank(2)._2.value == 0.07340)
      assert(infoFilesRank(3)._2.value == 0)
    }

  }
}

