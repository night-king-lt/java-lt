package proto.api;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.googlecode.protobuf.format.JsonFormat;
import proto.AddressBookProtos;

/**
 * @Author liu.teng
 * @Date 2020/8/21 11:16
 * @Version 1.0
 *    proto 反射详解: https://blog.csdn.net/JMW1407/article/details/107223287?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param
 */
public class PbMerge {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        AddressBookProtos.Person.Builder p = AddressBookProtos.Person.newBuilder();
        p.setId(1);
        p.setName("姚明");
//        p.addPhones(AddressBookProtos.Person.PhoneNumber.newBuilder().setNumber("110"));

        AddressBookProtos.Person.PhoneNumber.Builder number = AddressBookProtos.Person.PhoneNumber.newBuilder();
        number.setNumber("17899382746");
        AddressBookProtos.Person.Builder p1 = AddressBookProtos.Person.newBuilder();
        p1.setId(2);
        p1.setName("小明");
        p1.addPhones(number);
////
        p.mergeFrom(p1.build().toByteArray());
        p.mergeFrom(number.build().toByteArray());
        System.out.println(protobufToJson(p1.build()));

    }


    public static  String protobufToJson(Message message) {
        String jsonFormat = JsonFormat.printToString(message);
        return jsonFormat;
    }
}
