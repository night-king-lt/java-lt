package flink.window;

import flink.join.JoinTest;
import flink.source.ActionSource;
import flink.watermark.MyWatermark;
import model.ActionData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class windowDemo {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //定义数据源
        DataStream<ActionData> source = env.addSource(new JoinTest.ActionSource())
                .assignTimestampsAndWatermarks(new MyWatermark.innerWatermark());

//        source.print();

        source.keyBy(x -> x.getToken() + x.getUserId())
                .window(TumblingEventTimeWindows.of(Time.minutes(1)))
                .process(new ProcessWindowFunction<ActionData, Object, String, TimeWindow>() {
                    @Override
                    public void process(String s, Context context, Iterable<ActionData> iterable, Collector<Object> collector) throws Exception {
                        long count = 0;
                        for (ActionData a: iterable){
                            System.out.println(a);
                            count++;
                        }
                        TimeWindow window = context.window();
                        collector.collect("s: " + s + " window: start" + simpleDateFormat.format(window.getStart()) + " end: " + simpleDateFormat.format(window.getEnd()) + " count:" + count);
                    }
                })
                .print();


        env.execute("window test");
    }
}
