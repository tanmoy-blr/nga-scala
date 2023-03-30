package com.nag.repository

import scala.collection.mutable

class SensorStats {
  val sensorInfo : mutable.Map[String, SensorStatDto] = mutable.Map.empty
  val invalidSensors : mutable.Map[String, String] = mutable.Map.empty
  var totalRowsProcessed : Int = 0
  var totalFilesProcessed : Int = 0
  var totalFailedMeasurements : Int = 0
  def getSensorInfo(key: String) = {
    sensorInfo.get(key)
  }

  def putSensorInfo(key: String, countAndSum : (Int, Int)) : Unit = {
    val previousValue = sensorInfo.get(key)
    if (previousValue.isEmpty){
      sensorInfo.put(
        key = key,
        value=SensorStatDto(
          count=countAndSum._1,
          sum=countAndSum._2,
          min=countAndSum._2,
          max=countAndSum._2))
    }
    else {
      val newValue = previousValue.map(prev => {
        val min = if (previousValue.get.min < countAndSum._2) previousValue.get.min else countAndSum._2
        val max = if (previousValue.get.max > countAndSum._2) previousValue.get.max else countAndSum._2
        SensorStatDto(
          count = prev.count + countAndSum._1 ,
          sum = prev.sum + countAndSum._2,
          min, max)
      }).get

      sensorInfo.put(key = key, value = newValue)
    }
  }
}

object SensorStats extends SensorStats
