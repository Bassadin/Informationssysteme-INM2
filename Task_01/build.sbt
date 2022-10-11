ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "Task_01"
  )

// https://mvnrepository.com/artifact/com.h2database/h2
libraryDependencies += "com.h2database" % "h2" % "2.1.214" % Test
