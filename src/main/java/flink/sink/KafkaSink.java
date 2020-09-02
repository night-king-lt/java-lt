package flink.sink;

import flink.source.ActionSource;
import model.ActionData;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaProducer;

import java.util.Properties;

/**
 * @Author liu.teng
 * @Date 2020/8/21 16:55
 * @Version 1.0
 */
public class KafkaSink {
    private static final SimpleStringSchema schema = new SimpleStringSchema();
    private static final Properties properties = new Properties();

    static {
        properties.setProperty("bootstrap.servers", "localhost:9092");
    }
    public static void main(String[] args) throws Exception{
        Thread t1 = new Thread(() -> {
            try {
                prudoceData("show", "click");
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();

    }

    public static void prudoceData(String show, String click) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<ActionData> dataStream = env.addSource(new ActionSource.innerSource());
        FlinkKafkaProducer<String> showProducer = new FlinkKafkaProducer<>(show, schema, properties);
        FlinkKafkaProducer<String> clickProducer = new FlinkKafkaProducer<>(click, schema, properties);

        dataStream.filter(t -> t.getType() == 1).map(ActionData::toString).addSink(showProducer);
        dataStream.filter(t -> t.getType() == 2).map(ActionData::toString).addSink(clickProducer);
        env.execute();
    }

    public static void consumerData(String topic) throws Exception{
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        FlinkKafkaConsumer<String> consumer = new FlinkKafkaConsumer<>(topic, schema, properties);
        consumer.setStartFromEarliest();
        DataStream<String> dataStream = env.addSource(consumer);
        dataStream.print();

        env.execute();
    }

}
