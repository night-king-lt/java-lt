package flink.source;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class AddSourceTest {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<Tuple2<String,Integer>> dataStream = env.addSource(new Tuple2Source());
        dataStream.print();

        env.execute();
    }

    public static class Tuple2Source implements SourceFunction<Tuple2<String,Integer>>{

        @Override
        public void run(SourceContext<Tuple2<String, Integer>> sourceContext) throws Exception {
            sourceContext.collect(new Tuple2<>("java", 1));
            sourceContext.collect(new Tuple2<>("java", 1));
            sourceContext.collect(new Tuple2<>("java", 1));
            sourceContext.collect(new Tuple2<>("flink", 1));
            sourceContext.collect(new Tuple2<>("flink", 1));
            sourceContext.collect(new Tuple2<>("spark", 1));
        }

        @Override
        public void cancel() {

        }
    }
}
