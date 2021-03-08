package flink.TypeInfo;

import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;
import org.apache.flink.api.java.typeutils.WritableTypeInfo;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.Text;

import static org.apache.flink.api.common.typeinfo.BasicArrayTypeInfo.BYTE_ARRAY_TYPE_INFO;

/**
 * @Author liuteng
 * @Date 2020/7/16 7:48 下午
 * @Version 1.0
 */
public class TypeInfoStatic {
    public static final WritableTypeInfo<Text> textWritableType = new WritableTypeInfo<>(Text.class);
    public static final WritableTypeInfo<BytesWritable> bytesWritableType = new WritableTypeInfo<>(BytesWritable.class);
    public static final TypeInformation<Tuple2<Text, BytesWritable>> tuple2TypeInfo = new TupleTypeInfo<>(textWritableType, bytesWritableType);
    public static final TypeInformation<Tuple2<byte[], byte[]>> tuple2BytesTypeInfo = new TupleTypeInfo<>(BYTE_ARRAY_TYPE_INFO, BYTE_ARRAY_TYPE_INFO);
    public static final TypeInformation<Tuple2<String, byte[]>> tuple2StringTypeInfo = new TupleTypeInfo<>(BasicTypeInfo.STRING_TYPE_INFO, BYTE_ARRAY_TYPE_INFO);
    public static final TypeInformation<Tuple2<BytesWritable, BytesWritable>> tuple2StringAllTypeInfo = new TupleTypeInfo<>(bytesWritableType, bytesWritableType);
}
