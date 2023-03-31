ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "NGAScala",
      // https://mvnrepository.com/artifact/io.monix/monix-execution
      libraryDependencies ++= Seq( "io.monix" %% "monix-execution" % "3.4.1",
        // https://mvnrepository.com/artifact/org.scalatest/scalatest
        "org.scalatest" %% "scalatest" % "3.2.15" % Test,
        "org.scalamock" %% "scalamock" % "5.2.0" % Test


      )
)


