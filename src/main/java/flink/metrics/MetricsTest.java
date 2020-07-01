package flink.metrics;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/6/30
 */
public class MetricsTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<String> source = env.addSource(new SourceFunction<String>() {
            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                sourceContext.collect("h");
            }

            @Override
            public void cancel() {

            }
        });

        source.process(new MyMetrics());

        env.execute();

    }

    public static class MyMetrics extends ProcessFunction<String, String>{

        private transient int valueToExpose = -1;

        @Override
        public void open(Configuration parameters) throws Exception {
            super.open(parameters);
            valueToExpose = -1;
            getRuntimeContext()
                    .getMetricGroup()
                    .gauge("MyGauge", () -> valueToExpose);
        }

        @Override
        public void processElement(String s, Context context, Collector<String> collector) throws Exception {
            System.out.println(s + ": " +valueToExpose);
        }
    }
}
