package flink.qps;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.ParallelSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/6/30
 */
public class qpsTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment(10);
        env.enableCheckpointing(Long.parseLong("10000"));
        DataStream<Integer> source = env.addSource(new MySourceFunction()).setParallelism(1);

        source.map(new MyQpsMapFunction(20)).setParallelism(1).print().setParallelism(20);

        env.execute();
    }

    public static class MySourceFunction implements ParallelSourceFunction<Integer>{

        @Override
        public void run(SourceContext<Integer> sourceContext) throws Exception {
            while (true){
                sourceContext.collect( (int) (Math.random() * 100));
            }
        }

        @Override
        public void cancel() {

        }
    }

    public static class MyQpsMapFunction implements MapFunction<Integer, Integer> {

        private int counter;
        private long fisrtTime;
        private long maxCount;

        public MyQpsMapFunction(long maxCount){
            this.maxCount = maxCount;
        }

        @Override
        public Integer map(Integer value) throws Exception {
            counter++;
            if (counter == 1){
                fisrtTime = System.currentTimeMillis();
            }
            if (counter > maxCount){
                long lastTime = System.currentTimeMillis();
                long sleepTime = lastTime - fisrtTime;
                long timeWindow = 60000;
                if (sleepTime < timeWindow){
                    Thread.sleep(timeWindow - sleepTime);
                }
                counter = 0;
            }

            return value;
        }
    }
}
