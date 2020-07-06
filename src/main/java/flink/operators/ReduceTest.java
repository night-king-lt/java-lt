package flink.operators;

import flink.source.Tuple2Source;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class ReduceTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        DataStream<Tuple2<String, Integer>> dataStream = env.addSource(new Tuple2Source.innerSource());

        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = dataStream.keyBy(0);

        keyedStream.reduce(new Tuple2Reduce()).print();

        env.execute();
    }

    public static class Tuple2Reduce implements ReduceFunction<Tuple2<String, Integer>> {

        @Override
        public Tuple2<String, Integer> reduce(Tuple2<String, Integer> value1, Tuple2<String, Integer> value2) throws Exception {
            return new Tuple2<>(value1.f0, value1.f1 + value2.f1);
        }
    }
}
