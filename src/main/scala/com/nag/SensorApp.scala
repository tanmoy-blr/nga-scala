package com.nag

import com.nag.repository.SensorStats
import com.nag.service.AggregatorService

import java.io.File
import java.nio.file.{Files, Path}
import scala.jdk.CollectionConverters.IteratorHasAsScala

object SensorApp extends App{
  var path: Path = Path.of("")
  if (args.length == 0){
    println("Cannot execute without path information")
  }
  else{
    path = Path.of(args(0))
    main(path)
    AggregatorService.getReport()
  }

  def main(path: Path) = {
    Files.list(path).iterator()
      .asScala
      .filter(
        Files.isRegularFile(_))
      .foreach(filename => {
        AggregatorService.aggregate(filename.toFile)
      })
  }


  //main(Path.of("C:\\Users\\Home PC\\Downloads\\NGA-Scala-task\\data"))



}
