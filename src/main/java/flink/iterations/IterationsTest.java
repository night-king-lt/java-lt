package flink.iterations;

import org.apache.flink.api.common.functions.FilterFunction;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.tuple.Tuple3;
import org.apache.flink.api.java.tuple.Tuple5;
import org.apache.flink.streaming.api.collector.selector.OutputSelector;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.datastream.SplitStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.util.Collections;
import java.util.Random;

public class IterationsTest {
    private static final String ITERATE_FLAG = "y";
    private static final String OUTPUT_FLAG = "o";

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        DataStream<Long> someIntegers = env.generateSequence(1, 10);

        someIntegers.map(t -> "source: " + t).print();

        IterativeStream<Long> head = someIntegers.iterate();

        DataStream<Long> minusOne = head.map(t -> t - 1);

        final OutputTag<Long> outputTag = new OutputTag<Long>("iteration"){};

        SingleOutputStreamOperator<Long> result = minusOne.process(new ProcessFunction<Long, Long>() {
            @Override
            public void processElement(Long value, Context ctx, Collector<Long> out) throws Exception {
                if (value <= 0){
                    // 输出到下游
                    out.collect(value);
                }else{
                    // 输出到测游，再返回给迭代头
                    ctx.output(outputTag, value);
                }
            }
        });

        // 提供给该closeWith函数的DataStream将反馈到迭代头(head), 也就是将大于0的数据返回给迭代头再迭代运行，直到能输出到下游
        head.closeWith(result.getSideOutput(outputTag));

        result.map(t -> "sink: " + t).print();

        env.execute();

    }
}
