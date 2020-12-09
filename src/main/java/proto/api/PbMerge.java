package proto.api;

import com.google.protobuf.DescriptorProtos.*;
import com.google.protobuf.Descriptors;
import com.google.protobuf.DynamicMessage;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import proto.AddressBookProtos;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.google.protobuf.util.JsonFormat;

/**
 * @Author liu.teng
 * @Date 2020/8/21 11:16
 * @Version 1.0
 *    proto 反射详解: https://blog.csdn.net/JMW1407/article/details/107223287?utm_medium=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param&depth_1-utm_source=distribute.pc_relevant.none-task-blog-BlogCommendFromMachineLearnPai2-1.channel_param
 */
public class PbMerge {
    public static void main(String[] args) throws IOException {
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
        String result = protobufToJson(p1.build());
        System.out.println(result);

        AddressBookProtos.Person.Builder p3 = AddressBookProtos.Person.newBuilder();
        Message m = toProtoBean(p3, result);
        System.out.println(m);
    }

    public static Message toProtoBean(Message.Builder targetBuilder, String json) throws IOException {
        JsonFormat.parser().merge(json, targetBuilder);
        return targetBuilder.build();
    }

    public static  String protobufToJson(Message message) {
        String jsonFormat = PbJsonFormat.printToString(message);
        return jsonFormat;
    }

    public static void generateMsg() {
        // 构造
        com.google.protobuf.DescriptorProtos.FileDescriptorProto.Builder fileDesProtoBuilder = FileDescriptorProto
                .newBuilder();
        fileDesProtoBuilder.setSyntax("proto3");
        fileDesProtoBuilder.setName("food.proto");// proto文件
        Message.Builder fileOptionBuilder = FileOptions.newBuilder();

        com.google.protobuf.DescriptorProtos.DescriptorProto.Builder addMessageTypeBuilder = fileDesProtoBuilder
                .addMessageTypeBuilder();
        addMessageTypeBuilder.setName("Pair");// message name

        com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder fidldDesProtoBuilder = FieldDescriptorProto
                .newBuilder();
        fidldDesProtoBuilder.setName("key");
        fidldDesProtoBuilder.setTypeName("string");
        fidldDesProtoBuilder.setNumber(1);
        addMessageTypeBuilder.addField(fidldDesProtoBuilder.build());

        com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Builder fidldDesProtoBuilder1 = FieldDescriptorProto
                .newBuilder();
        fidldDesProtoBuilder1.setName("value");
        fidldDesProtoBuilder1.setTypeName("int32");
        fidldDesProtoBuilder1.setNumber(2);
        addMessageTypeBuilder.addField(fidldDesProtoBuilder1.build());

        FileDescriptorProto build = fileDesProtoBuilder.build();

        // 创建
        com.google.protobuf.DynamicMessage.Builder newBuilder = DynamicMessage
                .newBuilder(build);
        DynamicMessage build2 = newBuilder.build();
        System.out.println(build2);
        // TODO 上面的方式无法将消息生成对应的java类
        // 可以使用protoc命令生成java类，然后使用class load将类加载到jvm

        // 反射
        try {
            Class cl = Class.forName("com.lanjingling.pair.Food.$Pair");
            Method method = cl.getMethod("newBuilder");
            Object obj = method.invoke(null, new Object[] {});
            Message.Builder msgBuilder = (Message.Builder) obj;
            buildMessage(msgBuilder,new HashMap<>());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void buildMessage(Message.Builder msgBuilder, Map<String, String> map) {
        Descriptors.Descriptor descriptor = msgBuilder.getDescriptorForType();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            Descriptors.FieldDescriptor filedDescriptor = descriptor.findFieldByName(entry.getKey());
            if (filedDescriptor == null) {
                continue;
            }
            boolean isRepeated = filedDescriptor.isRepeated();
            Descriptors.FieldDescriptor.JavaType type = filedDescriptor.getJavaType();
            if (isRepeated) {
                String value = entry.getValue();
                String[] strArray = value.split(",");
                for (int i = 0; i < strArray.length; ++i) {
                    Object valueObject = getObject(strArray[i], type); // getObject
                    if (valueObject == null) {
                        continue;
                    }
                    msgBuilder.addRepeatedField(filedDescriptor, valueObject);
                }
            } else {
                Object valueObject = getObject(entry.getValue(), type);
                if (valueObject == null) {
                    continue;
                }
                msgBuilder.setField(filedDescriptor,
                        getObject(entry.getValue(), type));
            }
        }
        Message msg = msgBuilder.build();
    }

    private static Object getObject(String rawString,
                                    Descriptors.FieldDescriptor.JavaType type) {
        try {
            switch (type) {
                case INT:
                    return Integer.valueOf(rawString);
                case LONG:
                    return Long.valueOf(rawString);
                case FLOAT:
                    return Float.valueOf(rawString);
                case DOUBLE:
                    return Double.valueOf(rawString);
                case BOOLEAN:
                    return Boolean.valueOf(rawString);
                case STRING:
                    return rawString;
                default:
                    // BYTE_STRING, ENUM, MESSAGE 哈哈先支持以上这些啦
                    return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
