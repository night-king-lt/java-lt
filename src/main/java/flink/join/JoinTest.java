package flink.join;

import flink.source.ActionSource;
import flink.watermark.MyWatermark;
import model.ActionData;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/3
 */
public class JoinTest {
    public static void main(String[] args) {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //定义数据源
        DataStream<ActionData> source = env.addSource(new ActionSource.innerSource())
                .assignTimestampsAndWatermarks(new MyWatermark.innerWatermark());
    }
}
