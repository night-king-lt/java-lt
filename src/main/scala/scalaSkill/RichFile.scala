package scalaSkill

import scala.io.Source
import java.io.File
/**
 * @Author liu.teng
 * @Date 2020/8/17 19:05
 *  隐式转换
 */
//这里的RichFile相当于File的增强类 需要将被增强的类作为参数传入构造器中
class RichFile(val file: File) {
  def read = {
    Source.fromFile(file.getPath).mkString
  }
}

//implicit是隐式转换的关键字 这里定义一个隐式转换函数把当前类型转换成增强的类型
object Context {
  //File --> RichFile
  implicit def file2RichFile(file: File) = new RichFile(file)
}

object Hello_Implicit_Conversions {
  def main(args: Array[String]): Unit = {
    //导入隐式转换
    import Context.file2RichFile
    //File类本身没有read方法 通过隐式转换完成
    //这里的read方法是RichFile类中的方法  需要通过隐式转换File --> RichFile
    println(new File("src/main/resources/wordCount.txt").read)

  }
}