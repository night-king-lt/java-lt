package flink.kafka;

import flink.TypeInfo.TypeInfoStatic;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.connectors.kafka.KafkaDeserializationSchema;
import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @Author liu.teng
 * @Date 2021/2/24 17:17
 * @Version 1.0
 */
public class KvKafkaDeserializationSchema implements KafkaDeserializationSchema<Tuple2<byte[], byte[]>> {
    private static final long serialVersionUID = -1973138147115816388L;

    @Override
    public boolean isEndOfStream(Tuple2<byte[], byte[]> tuple2) {
        return false;
    }

    @Override
    public Tuple2<byte[], byte[]> deserialize(ConsumerRecord<byte[], byte[]> consumerRecord) throws Exception {
        return new Tuple2<>(consumerRecord.key(), consumerRecord.value());
    }

    @Override
    public TypeInformation<Tuple2<byte[], byte[]>> getProducedType() {
        return TypeInfoStatic.tuple2BytesTypeInfo;
    }
}
