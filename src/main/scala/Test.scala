import java.text.SimpleDateFormat

import org.joda.time.DateTime

/**
 * @author liuteng
 * @date 2020/7/9 
 * @version 1.0
 */
object Test {
  def main(args: Array[String]): Unit = {
    val date = new SimpleDateFormat("yyyyMMdd/HH").parse("20200709/08")
    val dateTime = new DateTime(date.getTime)
    val dt = dateTime.toString("yyyyMMdd")
    val hour = dateTime.toString("H")
    println(dt + " " + hour)
    println(dateTime.getMillis)
    val now = new SimpleDateFormat("yyyyMMdd/HH").format(System.currentTimeMillis())
    println(now)
  }
}
