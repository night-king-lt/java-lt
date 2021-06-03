package flink.state;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

/**
 * @Author liu.teng
 * @Date 2021/5/19 9:54
 * @Version 1.0
 */
public class StateTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        DataStream<Tuple2<String, Long>> d1 = env.addSource(new SourceFunction<Tuple2<String, Long>>() {
            @Override
            public void run(SourceContext<Tuple2<String, Long>> ctx) throws Exception {
                ctx.collect(new Tuple2<>("flink", System.currentTimeMillis()));
//                Thread.sleep(60000);
                ctx.collect(new Tuple2<>("java", System.currentTimeMillis()));
            }
            @Override
            public void cancel() {

            }
        });
        DataStream<Tuple2<String, Long>> d2 = env.addSource(new SourceFunction<Tuple2<String, Long>>() {
            @Override
            public void run(SourceContext<Tuple2<String, Long>> ctx) throws Exception {
//                Thread.sleep(120000);
                ctx.collect(new Tuple2<>("java", System.currentTimeMillis()));
                ctx.collect(new Tuple2<>("java", System.currentTimeMillis()));
                ctx.collect(new Tuple2<>("java", System.currentTimeMillis()));
            }
            @Override
            public void cancel() {

            }
        });
        
        d1.union(d2).keyBy(t -> t.f0).process(new KeyedProcessFunction<String, Tuple2<String, Long>, String>() {
            @Override
            public void processElement(Tuple2<String, Long> value, Context ctx, Collector<String> out) throws Exception {
                ctx.timerService().registerProcessingTimeTimer(System.currentTimeMillis() + 1);
                System.out.println(value.f0 + " " + value.f1 + " " + Thread.currentThread().getName());
            }

            @Override
            public void onTimer(long timestamp, OnTimerContext ctx, Collector<String> out) throws Exception {
                super.onTimer(timestamp, ctx, out);
            }
        });

        env.execute();
    }
}
