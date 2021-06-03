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

    /**
     * @param builder   需要填充特征的结果pb
     * @param fieldString   pb对应的字段名全路径
     * @param isRepeated  pb是否是 repeated
     * @param isFather  二进制数据格式对应的pb是否是父pb （一般redis存的都是父pb）（前提是isRepeated=true）
     * @param data   pb的二进制数据
     * @throws IOException
     */
    public static void fillFeatureByField(com.google.protobuf.Message.Builder builder, String fieldString, boolean isRepeated, boolean isFather, byte[] data) throws IOException {
        com.google.protobuf.Message.Builder curr = builder;

        String[] fields = fieldString.split("\\.");
        int len = isRepeated ? fields.length - 1 :fields.length;

        for (int i = 0; i < len; i++ ) {
            Descriptors.FieldDescriptor desc = curr.getDescriptorForType().findFieldByName(fields[i]);
            if (desc == null) {
                throw new IllegalArgumentException("未找到指定的属性, field path: " + fieldString + ", current field: " + fields[i]);
            }
            curr = curr.getFieldBuilder(desc);
        }

        if (isRepeated){
            Descriptors.FieldDescriptor desc = curr.getDescriptorForType().findFieldByName(fields[len]);
            if (desc.isRepeated()){
                if (isFather){  // 如果redis里面存的是父节点
                    curr.mergeFrom(data);
                }else{  // 如果redis里面存的就是当前节点
                    com.google.protobuf.Message.Builder repeated = curr.newBuilderForField(desc);
                    repeated.mergeFrom(data);
                    curr.addRepeatedField(desc, repeated.build());
                }
            }else{
                curr = curr.getFieldBuilder(desc);
                curr.mergeFrom(data);
            }
        }else{
            curr.mergeFrom(data);
        }
    }

    /**
     *   通过pb的字段层级，获得对应子pb的字符串
     * @param builder  pb数据
     * @param fieldString 字段层级
     * @param isRepeated 是否重复
     * @return
     */
    public static String getFeatureByField(com.google.protobuf.Message.Builder builder, String fieldString, boolean isRepeated){
        com.google.protobuf.Message.Builder curr = builder;
        com.google.protobuf.Message.Builder result;
        String[] fields = fieldString.split("\\.");
        int len = isRepeated ? fields.length - 1: fields.length;
        for (int i = 0; i < len; i++ ) {
            Descriptors.FieldDescriptor desc = curr.getDescriptorForType().findFieldByName(fields[i]);
            if (desc == null) {
                throw new IllegalArgumentException("未找到指定的属性, field path: " + fieldString + ", current field: " + fields[i]);
            }
            curr = curr.getFieldBuilder(desc);
        }
        if (isRepeated) {
            // 如果是重复字段，定义一个空的父pb
            result = curr.getDefaultInstanceForType().toBuilder();
            // 重复属性: 遍历 data.repeated, 将每一个元素 add 到 result 中
            String field = fields[fields.length - 1];
            Descriptors.FieldDescriptor desc = curr.getDescriptorForType().findFieldByName(field);
            int count = curr.getRepeatedFieldCount(desc);
            for (int i = 0; i < count; i++) {
                Object val = curr.getRepeatedField(desc, i);
                // 将重复的字段加入到父pb中
                result.addRepeatedField(desc, val);
            }
            return result.toString();
        } else {
            // 非 repeated 属性, 直接 merge
//            curr.mergeFrom(uncompressed);
            return curr.toString();
        }
    }

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
