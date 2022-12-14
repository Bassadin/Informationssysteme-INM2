ThisBuild / version := "0.1.0"
ThisBuild / scalaVersion := "2.13.10"
ThisBuild / description := "inm2-infsys-task03"
ThisBuild / name := "Informationssysteme Task 03"

val sparkVersion = "3.3.1"

lazy val root = (project in file("."))
    .settings(
      name := "Task_03"
    )

// https://mvnrepository.com/artifact/org.apache.spark/spark-core
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion

// https://mvnrepository.com/artifact/org.apache.spark/spark-sql
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion

// https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-core
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.19.0"


