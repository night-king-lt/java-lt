package proto.api;

import com.google.protobuf.Descriptors;
import org.xerial.snappy.Snappy;
import proto.AddressBookProtos;

import java.io.IOException;

/**
 * @Author liu.teng
 * @Date 2020/11/6 17:29
 * @Version 1.0
 */
public class ProtoTools {

    public static void fillFeatureByField(com.google.protobuf.Message.Builder builder, String fieldString, boolean isRepeated, byte[] data) throws IOException {
        com.google.protobuf.Message.Builder curr = builder;

        String[] fields = fieldString.split("\\.");
        for (int i = 0; i< fields.length - 1; i++ ) {
            Descriptors.FieldDescriptor desc = curr.getDescriptorForType().findFieldByName(fields[i]);
            if (desc == null) {
                throw new IllegalArgumentException("未找到指定的属性, field path: " + fieldString + ", current field: " + fields[i]);
            }
            curr = curr.getFieldBuilder(desc);
        }

        byte[] uncompressed;
        try{
            uncompressed = Snappy.uncompress(data);
        }catch (Exception e){
            uncompressed = data;
        }

        if (!isRepeated){
            Descriptors.FieldDescriptor desc = curr.getDescriptorForType().findFieldByName(fields[fields.length - 1]);
            if (desc.isRepeated()){
                curr.mergeFrom(uncompressed);
            }else{
                curr = curr.getFieldBuilder(desc);
                curr.mergeFrom(uncompressed);
            }
        }


    }

    public static void main(String[] args) throws IOException {
        AddressBookProtos.AddressBook.Builder p1 = AddressBookProtos.AddressBook.newBuilder();

        // TODO  测试 重复（repeated） 字段
//        AddressBookProtos.Person.PhoneNumber.Builder phoneNumber = AddressBookProtos.Person.PhoneNumber.newBuilder();
//        phoneNumber.setNumber("111111111");
//        fillFeatureByField(p1, "people.phones", false, phoneNumber.build().toByteArray());

        // TODO  测试 不重复字段
        AddressBookProtos.AddressBook.Builder p2 = AddressBookProtos.AddressBook.newBuilder();
        AddressBookProtos.Person.Builder person = AddressBookProtos.Person.newBuilder();
        person.setId(11111);
        person.setName("flink");
        p2.addPeople(person);
        fillFeatureByField(p1, "people", false, p2.build().toByteArray());

        System.out.println(p1);
    }
}
