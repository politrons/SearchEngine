import sbt._

object Dependencies {

  lazy val CommonDependencies = Seq("org.scalatest" %% "scalatest" % "3.0.1" % "test")

//  lazy val InfrastructureDependencies = Seq(cassandra_driver, postgresql_driver, kafka_client)
//
//  lazy val ApiDependencies = Seq(play)
}