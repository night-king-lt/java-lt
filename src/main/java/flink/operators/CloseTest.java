package flink.operators;

import flink.source.StringSource;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.util.Collector;

/**
 * @Author liu.teng
 * @Date 2020/9/10 14:50
 * @Version 1.0
 */
public class CloseTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();

        DataStream<String> ds = env.addSource(new StringSource.innerSource());
        ds.process(new ProcessFunction<String, String>() {
            @Override
            public void open(Configuration parameters) throws Exception {
                System.out.println("open");
            }

            @Override
            public void processElement(String s, Context context, Collector<String> collector) throws Exception {
                collector.collect(s);
                throw new NullPointerException();
            }

            @Override
            public void close() throws Exception {
                System.out.println("close");
            }
        });

        env.execute();
    }
}
