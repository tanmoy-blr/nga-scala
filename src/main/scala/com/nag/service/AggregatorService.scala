package com.nag.service

import com.nag.repository.SensorStats
import java.io.{BufferedReader, File, FileReader}

class AggregatorService(stats: SensorStats){

  val NaN = Int.MinValue
  val skipHeader = true

  def aggregate(filename : File) = {
    val br = new BufferedReader(new FileReader(filename))
    var line = Option(br.readLine())
    if (skipHeader) {
      line = Option(br.readLine())
    }
    stats.totalFilesProcessed = stats.totalFilesProcessed + 1
    while (line.nonEmpty) {
      //println(s"Processing ${filename} - row :: ${line}")
      stats.totalRowsProcessed = stats.totalRowsProcessed + 1
      val lineArray = line.get.split(",")
      val sensor = lineArray(0)
      val reading = lineArray(1)

      if (!reading.equals("NaN")) {
        val existingReading = stats.getSensorInfo(key = sensor)
        val ifExistsOnInvalidList = stats.invalidSensors.get(key = sensor)
        if (existingReading.isEmpty) {
          val sum = reading.toInt
          val count = 1
          stats.putSensorInfo(key = sensor, countAndSum = (count, sum))
        }
        else{
          //println("Existing reading : " , existingReading , "current reading : ", reading)
//          val sum = existingReading.get._2 + reading.toInt
//          val count = existingReading.get._1 + 1
          stats.putSensorInfo(key=sensor, countAndSum = (1,reading.toInt))
        }
        if (ifExistsOnInvalidList.nonEmpty){
          stats.invalidSensors.remove(sensor)
        }
    }
      else{
        stats.totalFailedMeasurements = stats.totalFailedMeasurements + 1
        if (stats.sensorInfo.get(sensor).isEmpty){
          stats.invalidSensors.put(sensor, "NaN")
        }

      }
      line = Option(br.readLine())
  }
    //println("Stats (Intermediate) : ", stats.sensorInfo.toList)
  }

  def getReport() = {
    val avgHumidityReport = stats.sensorInfo.map(elem => {
      ((elem._2.sum / elem._2.count), (elem._1, elem._2.min, elem._2.max))
    }).toList.sortBy(t => t._1).reverse
      .map(sensorData => s"${sensorData._2._1},${sensorData._2._2}, ${sensorData._1}, ${sensorData._2._3}")
      .mkString("\n")
    val invalidSensorReport = stats.invalidSensors.toList.map(invalidSensor => {
      s"${invalidSensor._1}, ${invalidSensor._2}, ${invalidSensor._2}, ${invalidSensor._2}"
    })
      .mkString("\n")
    println(s"Num of processed files: ${stats.totalFilesProcessed}")
    println(s"Num of processed measurements: ${stats.totalRowsProcessed}")
    println(s"Num of failed measurements: ${stats.totalFailedMeasurements}")
    println(
      s"""Sensors with highest avg humidity:
         |sensor_id, min, avg, max
         |${avgHumidityReport}
         |${invalidSensorReport}
         |""".stripMargin)
  }
}

object AggregatorService extends AggregatorService(SensorStats)
