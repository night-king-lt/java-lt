import java.text.SimpleDateFormat

import org.joda.time.DateTime

/**
 * @author liuteng
 * @date 2020/7/9 
 * @version 1.0
 */
object Test {

  val timeFormat = "yyyyMMdd/HH/m0/"

  def main(args: Array[String]): Unit = {
    val date = new SimpleDateFormat("yyyyMMdd/HH/mm").parse("20210721/14/05")
    val dateTime = new DateTime(date.getTime)
    println(getMinutePath(dateTime))
  }

  def getMinutePath(d: DateTime): String ={
    val hour = d.toString("yyyyMMdd/HH/")
    val mimute = d.toString("mm").charAt(0) + "0/"
    hour + mimute
  }
}
