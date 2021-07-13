package com.politrons.engine

import org.scalatest.{BeforeAndAfterAll, FeatureSpec, GivenWhenThen}

import java.nio.file.{Path, Paths}

class SearchEngineSpec extends FeatureSpec with GivenWhenThen with BeforeAndAfterAll {

  feature(" Search engine") {

    scenario("All files properly loaded") {

      Given("a folder path where we have the files")
      val resourcesPath = getClass.getResource("/files")
      val filesPath = resourcesPath.getPath
      When("I instantiate the SearchEngine")
      val engine = SearchEngine(filesPath)
      Then("I FileInfo list is loaded in memory filter and properly")
      val files = engine.files
      assert(files.nonEmpty)
      assert(files.size == 3)

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

    scenario("100% 50% and 0% rank files with 'hello world' ") {

      Given("a search engine with one hello world file loaded")
      val resourcesPath = getClass.getResource("/files")
      val filesPath = resourcesPath.getPath
      When("I search for hello world sentence")
      val engine = SearchEngine(filesPath)
      Then("I receive the 100% rank")
      val infoFilesRank = engine.search("hello world")
      assert(infoFilesRank.size == 3)

    }
  }
}
