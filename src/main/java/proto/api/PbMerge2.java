package proto.api;

import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import proto.AddressBookProtos;

import java.io.IOException;

/**
 * @Author liu.teng
 * @Date 2020/8/21 11:16
 * @Version 1.0
 *    proto 反射详解: https://blog.csdn.net/JMW1407/article/details/107223287?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param
 */
public class PbMerge2 {
    public static void main(String[] args) throws IOException {
        AddressBookProtos.Person.Builder p = AddressBookProtos.Person.newBuilder();
        p.setId(1);
        p.setName("姚明");

        AddressBookProtos.Person.PhoneNumber.Builder number = AddressBookProtos.Person.PhoneNumber.newBuilder();
        number.setNumber("17899382746");

        p.mergeFrom(number.build().toByteArray());
        String result = protobufToJson(p.build());
        System.out.println(result);

    }

    public static  String protobufToJson(Message message) {
        String jsonFormat = PbJsonFormat.printToString(message);
        return jsonFormat;
    }
}
