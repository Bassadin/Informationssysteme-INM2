ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.10"
ThisBuild / description := "inm2-infsys-task04"
ThisBuild / name := "Informationssysteme Task 04"

lazy val root = (project in file("."))
    .settings(
      name := "Task_04"
    )

// https://mvnrepository.com/artifact/io.spray/spray-json
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"