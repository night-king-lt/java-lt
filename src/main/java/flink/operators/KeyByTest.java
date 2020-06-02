package flink.operators;

import flink.source.AddSourceTest;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class KeyByTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        DataStream<Tuple2<String, Integer>> dataStream = env.addSource(new AddSourceTest.Tuple2Source());

        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = dataStream.keyBy(0);

        keyedStream.print();

        env.execute();
    }
}
