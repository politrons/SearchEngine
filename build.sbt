import Dependencies.CommonDependencies

name := "search_engine"

//version := "0.1"
//
//scalaVersion := "2.12.8"

lazy val commonSettings = Seq(
  organization := "com.politrons",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.8",
  scalacOptions := Seq(
    "-deprecation",
    "-feature"
  ),
  libraryDependencies ++= CommonDependencies
)

lazy val domain = project
  .in(file("domain"))
  .settings(
    name := "domain",
    assembly / test := (Test / test).value,
    commonSettings
  )

lazy val infrastructure = project
  .in(file("infrastructure"))
  .dependsOn(domain)
  .settings(
    name := "infrastructure",
    assembly / test := (Test / test).value,
    commonSettings
  )

lazy val application = project
  .in(file("application"))
  .dependsOn(domain, infrastructure)
  .settings(
    name := "application",
    assembly / test := (Test / test).value,
    assembly / mainClass := Some("com.politrons.app.SearchApp"),
    commonSettings
  )


//lazy val assemblySettings = Seq(
//  assemblyJarName in assembly := name.value + ".jar",
//  assemblyMergeStrategy in assembly := {
//    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
//    case _                             => MergeStrategy.first
//  }
//)

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)

