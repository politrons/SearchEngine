package com.politrons.service

import com.politrons.engine.SearchEngine
import com.politrons.model.{FileInfo, Rank}
import org.scalamock.scalatest.MockFactory
import org.scalatest.{BeforeAndAfterAll, FeatureSpec, GivenWhenThen}

class SearchServiceSpec extends FeatureSpec with GivenWhenThen with BeforeAndAfterAll with MockFactory {

  feature(" Search Service") {

    scenario("search service receive and return properly data") {

      Given("a search engine with one hello world file loaded")
      val mockEngine = mock[SearchEngine]

      val list = List((FileInfo("foo.txt", Map()), Rank(100)))
      (mockEngine.search _).expects("hello world").returning(list).once()

      val service = SearchService(mockEngine)
      When("I search for hello world sentence")
      val buffer = service.searchInput("hello world")
      Then("I receive the output with correct result")
      assert(buffer.toString.contains("foo.txt:100%"))
    }

    scenario("search service receive and return only 10 elements") {

      Given("a search engine with one hello world file loaded")
      val mockEngine = mock[SearchEngine]

      val list = List(
        (FileInfo("foo1.txt", Map()), Rank(100)),
        (FileInfo("foo2.txt", Map()), Rank(99)),
        (FileInfo("foo3.txt", Map()), Rank(98)),
        (FileInfo("foo4.txt", Map()), Rank(97)),
        (FileInfo("foo5.txt", Map()), Rank(96)),
        (FileInfo("foo6.txt", Map()), Rank(95)),
        (FileInfo("foo7.txt", Map()), Rank(94)),
        (FileInfo("foo8.txt", Map()), Rank(93)),
        (FileInfo("foo9.txt", Map()), Rank(92)),
        (FileInfo("foo10.txt", Map()), Rank(91)),
        (FileInfo("foo11.txt", Map()), Rank(90)),
        (FileInfo("foo12.txt", Map()), Rank(89)),
        (FileInfo("foo13.txt", Map()), Rank(88)),
        (FileInfo("foo14.txt", Map()), Rank(87)),
        (FileInfo("foo15.txt", Map()), Rank(86)),
      )
      (mockEngine.search _).expects("hello world").returning(list).once()

      val service = SearchService(mockEngine)
      When("I search for hello world sentence")
      val buffer = service.searchInput("hello world")
      Then("I receive the output with correct result")
      assert(buffer.toString.contains("foo1.txt:100%"))
      assert(buffer.toString.contains("foo2.txt:99%"))
      assert(buffer.toString.contains("foo3.txt:98%"))
      assert(buffer.toString.contains("foo4.txt:97%"))
      assert(buffer.toString.contains("foo5.txt:96%"))
      assert(buffer.toString.contains("foo6.txt:95%"))
      assert(buffer.toString.contains("foo7.txt:94%"))
      assert(buffer.toString.contains("foo8.txt:93%"))
      assert(buffer.toString.contains("foo9.txt:92%"))
      assert(buffer.toString.contains("foo10.txt:91%"))
      assert(!buffer.toString.contains("foo11.txt:90%"))
      assert(!buffer.toString.contains("foo12.txt:89%"))
      assert(!buffer.toString.contains("foo13.txt:88%"))
      assert(!buffer.toString.contains("foo14.txt:87%"))
      assert(!buffer.toString.contains("foo15.txt:86%"))

    }
  }
}
