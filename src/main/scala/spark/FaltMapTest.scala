package spark

import org.apache.spark.{SparkConf, SparkContext}

/**
 * @Author liu.teng
 * @Date 2020/8/18 15:14
 * @Version 1.0
 */
object FaltMapTest {
  def main(args: Array[String]): Unit = {
    val sc = new SparkContext(new SparkConf().setAppName("test").setMaster("local[8]"))

  }

}
