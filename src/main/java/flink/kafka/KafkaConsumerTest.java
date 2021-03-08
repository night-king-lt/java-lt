package flink.kafka;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.hadoop.io.BytesWritable;

import java.util.Properties;

import static flink.kafka.KafkaProducerTest.kvConsumerTest;

/**
 * @Author liu.teng
 * @Date 2021/2/26 14:11
 * @Version 1.0
 */
public class KafkaConsumerTest {

    private static final Properties properties = new Properties();
    private static final String topic = "test";

    static {
        properties.setProperty("bootstrap.servers", "localhost:9092");
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

//        stringTest(env);
        kvConsumerTest(env);
        env.execute();
    }

    public static void kvConsumerTest(StreamExecutionEnvironment env){

        FlinkKafkaConsumer<Tuple2<BytesWritable, BytesWritable>> kafkaConsumer = new FlinkKafkaConsumer<>(topic,
                new KvKafkaDeserializationSchema3(), properties);

        env.addSource(kafkaConsumer).map(t -> new String(t.f0.getBytes()) + new String(t.f1.getBytes())).print();
    }
}
