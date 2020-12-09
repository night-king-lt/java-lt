package spark.join

import org.apache.spark.Partitioner

/**
 * @Author liu.teng
 * @Date 2020/12/9 10:34
 * @Version 1.0
 */
class StudentPartitioner(partitions: Int) extends Partitioner {

  require(partitions >= 0, s"Number of partitions ($partitions) cannot be negative.")

  override def numPartitions: Int = partitions

  override def getPartition(key: Any): Int = {
    val k = key.asInstanceOf[StudentKey]
    Math.abs(k.grade.hashCode()) % numPartitions
  }


}
