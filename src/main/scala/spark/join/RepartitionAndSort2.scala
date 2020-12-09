package spark.join

import org.apache.spark.rdd.RDD
import org.apache.spark.{HashPartitioner, Partitioner, SparkConf, SparkContext}

/**
 * @Author liu.teng
 * @Date 2020/12/9 10:45
 * @Version 1.0
 */
object RepartitionAndSort2 {
  def main(args: Array[String]): Unit = {
    //设置master为local，用来进行本地调试
    val conf = new SparkConf().setAppName("Student_partition_sort").setMaster("local[4]")
    val sc = new SparkContext(conf)
    val student_array = Array(
      "c001,n003,chinese,59",
      "c002,n004,english,79",
      "c002,n004,chinese,13",
      "c001,n001,english,88",
      "c001,n002,chinese,10",
      "c002,n006,chinese,29",
      "c001,n001,chinese,54",
      "c001,n002,english,32",
      "c001,n003,english,43",
      "c002,n005,english,80",
      "c002,n005,chinese,48",
      "c002,n006,english,69"
    )
    val student_rdd = sc.parallelize(student_array)
    val student_rdd2: RDD[((String, Int), String)] = student_rdd.map(line => line.split(",")).map(t => {((t(0), t(3).toInt), t(2))})
    student_rdd2.repartitionAndSortWithinPartitions(new KeyBasePartitioner(4)).mapPartitionsWithIndex( (k, v) => {
      println(k + ": ")
      v.foreach(t => println(t))
      v
    }).count()
  }

  class KeyBasePartitioner(partitions: Int) extends Partitioner {
    override def numPartitions: Int = partitions
    override def getPartition(key: Any): Int = {
      val k = key.asInstanceOf[(String, Int)]._1
      println("key: " + k)
      Math.abs(k.hashCode() % numPartitions)
    }
  }

}
