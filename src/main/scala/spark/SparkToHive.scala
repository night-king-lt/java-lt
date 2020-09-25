package spark

import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SparkSession}

/**
 * @Author liu.teng
 * @Date 2020/9/25 20:19
 * @Version 1.0
 */
object SparkToHive {
  def main(args: Array[String]): Unit = {
    val sparkSession = SparkSession.builder().appName("check-data-lt").enableHiveSupport().getOrCreate()
    val sc = sparkSession.sparkContext

    // 定义hive表的表结构
    val schema = StructType(
      List(
        StructField("user", StringType, true),
        StructField("name", StringType, true)
      )
    )

    val dt = 20200925
    val hour = 18
    // 表对应hdfs 路径
    val outPath = "hdfs:tablename/dt=20200925/hour=18"

    val rowRdd = sc.parallelize(List((1,"hadoop"), (2, "flink")))
      .map(t => Row(t._1, t._2))

    val sqlContext = sparkSession.sqlContext
    val pSchemaRDD = sqlContext.createDataFrame(rowRdd, schema)
    // 将数据写入到 hdfs  orc格式
    pSchemaRDD.write.format("orc").save(outPath)

    // 添加分区，不然hive表查不出来数据
    val addPartitionsSql = s"alter table table_name add partition(dt='$dt',hour='$hour')"
    println("add partitions: " + addPartitionsSql)
    sqlContext.sql(addPartitionsSql)

    sc.stop()
  }
}
