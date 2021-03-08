package flink.kafka;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.connectors.kafka.KafkaSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.annotation.Nullable;

/**
 * @Author liu.teng
 * @Date 2021/2/24 17:04
 * @Version 1.0
 */
public class KvKafkaSerializationSchema implements KafkaSerializationSchema<Tuple2<byte[], byte[]>> {
    private static final long serialVersionUID = -7870862817146015116L;

    private final String topic;

    public KvKafkaSerializationSchema(String topic){
        this.topic = topic;
    }

    @Override
    public ProducerRecord<byte[], byte[]> serialize(Tuple2<byte[], byte[]> data, @Nullable Long aLong) {
        return new ProducerRecord<>(this.topic, data.f0, data.f1);
    }
}
