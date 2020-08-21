package spark

import org.apache.spark.{SparkConf, SparkContext}
/**
 * @Author liu.teng
 * @Date 2020/8/17 19:49
 * @Version 1.0
 */
object WordCount {
  def main(args: Array[String]): Unit = {
     val sc = getLocalSparkContext
      sc.sequenceFile[String, Array[Byte]]("asfs")
  }

  def getLocalSparkContext: SparkContext = {
    val sc = new SparkContext(new SparkConf().setAppName("test").setMaster("local[8]"))
    sc.setLogLevel("ERROR")
    sc
  }
}
