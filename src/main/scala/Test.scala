import java.text.SimpleDateFormat

import org.joda.time.DateTime

/**
 * @author liuteng
 * @date 2020/7/9 
 * @version 1.0
 */
object Test {
  def main(args: Array[String]): Unit = {
    val child = "asdf"
    val key = child match {
      case null => "null"
      case _ => "not null"
    }
    println(key)
  }
}
