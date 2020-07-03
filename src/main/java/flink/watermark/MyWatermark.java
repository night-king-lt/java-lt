package flink.watermark;

import flink.source.ActionSource;
import model.ActionData;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.watermark.Watermark;

import java.text.SimpleDateFormat;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/3
 */
public class MyWatermark {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<ActionData> dataStream = env.addSource(new ActionSource.innerSource()).assignTimestampsAndWatermarks(new innerWatermark());
        dataStream.print();

        env.execute();
    }

    public static class innerWatermark implements AssignerWithPeriodicWatermarks<ActionData> {
        static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        private long currentMaxTimestamp;
        @Override
        public Watermark getCurrentWatermark() {
            // 5 seconds
            long maxDelayTime = 10000L;
            return new Watermark(currentMaxTimestamp - maxDelayTime);
        }

        @Override
        public long extractTimestamp(ActionData data, long l) {
            long dataEventTime = data.getEventTime();
            currentMaxTimestamp = Math.max(dataEventTime, currentMaxTimestamp);
//            System.out.println("watermark: " + simpleDateFormat.format(getCurrentWatermark().getTimestamp()) + " currmax: " + simpleDateFormat.format(currentMaxTimestamp) );
            return currentMaxTimestamp;
        }
    }
}
