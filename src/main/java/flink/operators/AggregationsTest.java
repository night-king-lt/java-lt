package flink.operators;

import flink.source.Tupler2Source;
import org.apache.flink.api.common.functions.AggregateFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.KeyedStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class AggregationsTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        DataStream<Tuple2<String, Integer>> dataStream = env.addSource(new Tupler2Source.innerSource());

        KeyedStream<Tuple2<String, Integer>, Tuple> keyedStream = dataStream.keyBy(0);

        keyedStream.sum(1).print();

        env.execute();
    }

    public static class MyAggregate implements AggregateFunction<Tuple2<String, Integer>, Tuple2<String, Integer>, Tuple2<String, Integer>> {


        @Override
        public Tuple2<String, Integer> createAccumulator() {
            return null;
        }

        @Override
        public Tuple2<String, Integer> add(Tuple2<String, Integer> value, Tuple2<String, Integer> accumulator) {
            return null;
        }

        @Override
        public Tuple2<String, Integer> getResult(Tuple2<String, Integer> accumulator) {
            return null;
        }

        @Override
        public Tuple2<String, Integer> merge(Tuple2<String, Integer> a, Tuple2<String, Integer> b) {
            return null;
        }
    }
}
