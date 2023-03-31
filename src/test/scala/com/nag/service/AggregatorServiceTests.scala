package com.nag.service

import com.nag.SensorApp
import com.nag.repository.SensorStats
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.{BeforeAndAfterAll}

import java.io.File
import java.io.FileWriter
import java.nio.file.{Files, Path, Paths}
import scala.collection.mutable


class AggregatorServiceTests extends AnyFlatSpec with BeforeAndAfterAll {

  val testFileName = "test-prefix"
  val configMap = mutable.Map.empty[String, File]

  override def beforeAll() = {
    val tempDir = Files.createTempDirectory(testFileName)
    print("The temporary directory = " +(tempDir))
    val tempFile = new File(osAwareFilename(tempDir, testFileName))
    tempFile.createNewFile()
    configMap.put(testFileName, tempFile)
    val myWriter = new FileWriter(tempFile)
    myWriter.write("sensor-id,humidity\ns1,10\ns2,88\ns1,NaN")
    myWriter.close()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    val tempFile = configMap.get(testFileName).get
    tempFile.delete()
  }

  private def osAwareFilename(inputPath : Path, filename : String) : String = {
    if (System.getProperty("os.name").contains("Windows")) {
      inputPath.toFile.getPath + "\\" + filename
    }
    else {
      inputPath.toFile.getPath + "/" + filename
    }
  }

  "The Aggregator Service" should "sum sensor s1 and s2 records ignoring NaN " in  {
    val fileForTest = configMap.get(testFileName).get
    println("File for test :: "+ fileForTest.toPath.getParent)
    val aggregatorService = SensorApp.main(fileForTest.toPath.getParent)
    assert(SensorStats.totalRowsProcessed == 3)
    assert(SensorStats.totalFailedMeasurements == 1)
    assert(SensorStats.invalidSensors.keys.size == 0)
    assert(SensorStats.sensorInfo.get("s1").get.sum == 10)

  }

}
