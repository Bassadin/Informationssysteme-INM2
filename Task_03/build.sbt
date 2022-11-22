ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.10"
ThisBuild / description := "inm2-infsys-task03"
ThisBuild / name := "Informationssysteme Task 03"

val sparkVersion = "3.3.1"

lazy val root = (project in file("."))
    .settings(
      name := "Task_03"
    )

// https://mvnrepository.com/artifact/io.spray/spray-json
libraryDependencies += "io.spray" %% "spray-json" % "1.3.6"

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion % "provided"

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion % "provided"
