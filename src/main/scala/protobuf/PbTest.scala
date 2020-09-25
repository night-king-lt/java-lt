package protobuf

import com.googlecode.protobuf.format.JsonFormat
import proto.AddressBookProtos
import proto.api.PbJsonFormat

/**
 * @Author liu.teng
 * @Date 2020/9/25 10:27
 * @Version 1.0
 */
object PbTest {
  def main(args: Array[String]): Unit = {
    val p = AddressBookProtos.Person.newBuilder
    p.setId(1)
    p.setName("姚明")
    println(PbJsonFormat.printToString(p.build()))
  }
}
