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
            sourceContext.collect("java,1");
            sourceContext.collect("hadoop,1");
            sourceContext.collect("java,1");
            sourceContext.collect("flink,1");
            sourceContext.collect("flink,1");
            sourceContext.collect("hahah,1");
        }

        @Override
        public void cancel() {

        }
    }
}
