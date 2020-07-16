package flink.source;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

public class StringSource {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<String> dataStream = env.addSource(new innerSource());
        dataStream.print();

        env.execute();
    }

    public static class innerSource implements SourceFunction<String>{

        @Override
        public void run(SourceContext<String> sourceContext) throws Exception {
            sourceContext.collect("java");
            sourceContext.collect("hadoop");
            sourceContext.collect("java");
            sourceContext.collect("flink");
            sourceContext.collect("hahah");
        }

        @Override
        public void cancel() {

        }
    }
}