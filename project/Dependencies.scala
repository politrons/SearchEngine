import sbt._

object Dependencies {

  lazy val CommonDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % "test",
    "org.scalamock" %% "scalamock" % "5.1.0" % Test
  )


}