package flink.kafka;

import flink.source.ActionSource;
import model.ActionData;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;
import org.apache.kafka.clients.producer.KafkaProducer;

import java.util.Properties;

/**
 * @Author liu.teng
 * @Date 2021/2/26 12:56
 * @Version 1.0
 */
public class KafkaProducerTest {

    private static final SimpleStringSchema schema = new SimpleStringSchema();
    private static final Properties properties = new Properties();
    private static final String topic = "test";

    static {
        properties.setProperty("bootstrap.servers", "localhost:9092");
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        kvProducerTest(env);
        env.execute();
    }

    public static void stringTest(StreamExecutionEnvironment env)  {
        DataStream<ActionData> dataStream = env.addSource(new ActionSource.innerSource());
        FlinkKafkaProducer<String> showProducer = new FlinkKafkaProducer<>(topic, schema, properties);

        dataStream.map(ActionData::toString).addSink(showProducer);
        dataStream.map(ActionData::toString).print();
    }

    public static void kvProducerTest(StreamExecutionEnvironment env) {
        DataStream<Tuple2<byte[], byte[]>> source = env.addSource(new SourceFunction<Tuple2<byte[], byte[]>>() {
            @Override
            public void run(SourceContext<Tuple2<byte[], byte[]>> sourceContext) throws Exception {
                for (int i = 0; i < 10; i++){
                    Tuple2<byte[], byte[]> next = new Tuple2<>((i+":").getBytes(), "flink".getBytes());
                    sourceContext.collect(next);
                }
            }

            @Override
            public void cancel() {

            }
        });

        KvKafkaSerializationSchema schema = new KvKafkaSerializationSchema(topic);
        FlinkKafkaProducer<Tuple2<byte[], byte[]>> kafkaSink = new FlinkKafkaProducer<>(topic, schema, properties, FlinkKafkaProducer.Semantic.EXACTLY_ONCE);

        source.addSink(kafkaSink);

    }

    public static void kvConsumerTest(StreamExecutionEnvironment env){

        FlinkKafkaConsumer<Tuple2<String, byte[]>> kafkaConsumer = new FlinkKafkaConsumer<>(topic,
                new KvKafkaDeserializationSchema2(), properties);

        env.addSource(kafkaConsumer).map(t -> new String(t.f0) + new String(t.f1)).print();
    }
}
