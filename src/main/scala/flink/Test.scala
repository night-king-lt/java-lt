package flink

import java.text.SimpleDateFormat

import org.joda.time.DateTime

object Test {
  def main(args: Array[String]): Unit = {
    val dateString = "20200322/15"
    val date = new SimpleDateFormat("yyyyMMdd/HH").parse(dateString)
    val dateTime = new DateTime(date.getTime)
    println("aaa" + dateTime.toString("yyyyMMdd/HH/*/*/"))
  }
}
