ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.10"

lazy val root = (project in file("."))
  .settings(
    name := "NGAScala",
      // https://mvnrepository.com/artifact/io.monix/monix-execution
      libraryDependencies ++= Seq( "io.monix" %% "monix-execution" % "3.4.1",
        // https://mvnrepository.com/artifact/com.eed3si9n/sbt-assembly
        //"com.eed3si9n" %% "sbt-assembly" % "sbt0.10.1_0.6"

      )
)


