package scalaSkill

/**
 * @Author liu.teng
 * @Date 2020/8/18 14:57
 * @Version 1.0
 */
object FlatmapTest {
  def main(args: Array[String]): Unit = {

    var words = Set("hive", "hbase", "redis")
    val result = words.flatMap(x => x.toUpperCase)
    println(result) // 输出结果：Set(E, A, I, V, B, H, R, D, S
  }

}
