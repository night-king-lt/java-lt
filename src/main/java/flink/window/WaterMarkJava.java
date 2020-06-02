package flink.window;

import dao.ActionData;
import org.apache.commons.lang3.time.FastDateFormat;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.api.java.typeutils.TupleTypeInfo;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;

public class WaterMarkJava {

    private static FastDateFormat  sdf= FastDateFormat.getInstance("yyyy-MM-dd HH:mm:ss:SSS");

    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStream<Tuple2<String, Long>> source = env.socketTextStream("localhost",9000, "\n")
                .map(x -> {
                    String[] items = x.split(":");
                    return new Tuple2<>(items[0],Long.parseLong(items[1]));
                }).returns(new TupleTypeInfo<>(BasicTypeInfo.STRING_TYPE_INFO, BasicTypeInfo.LONG_TYPE_INFO))
                .assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<Tuple2<String, Long>>() {
                    long currentMaxTimestamp = 0L;
                    long maxOutOfOrderness = 3000L;
                    long lastEmittedWatermark = 0L;
                    @Nullable
                    @Override
                    public Watermark getCurrentWatermark() {
                        long potentialWM = currentMaxTimestamp - maxOutOfOrderness;
                        // 保证水印能依次递增
                        if (potentialWM >= lastEmittedWatermark) {
                            lastEmittedWatermark = potentialWM;
                        }
                        return new Watermark(lastEmittedWatermark);
                    }

                    @Override
                    public long extractTimestamp(Tuple2<String, Long> t, long l) {
                        long time = t.f1;
                        if (time > currentMaxTimestamp) {
                            currentMaxTimestamp = time;
                        }
                        String outData = String.format("key: %s    EventTime: %s    waterMark:  %s",
                                t.f0,
                                sdf.format(time),
                                sdf.format(getCurrentWatermark().getTimestamp()));
                        System.out.println(outData);
                        return time;
                    }
                });

        source.keyBy(x -> x.f0)
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
                .apply(new WindowFunction<Tuple2<String, Long>, Object, String, TimeWindow>() {
                    @Override
                    public void apply(String s, TimeWindow timeWindow, Iterable<Tuple2<String, Long>> iterable, Collector<Object> collector) throws Exception {
                        StringBuffer result = new StringBuffer();
                        for (Tuple2<String, Long> t: iterable){
                            result.append(t.f0).append("-");
                        }
                        String outData = String.format("key: %s    data: %s    startTime:  %s    endTime:  %s",
                                s,
                                result.toString(),
                                sdf.format(timeWindow.getStart()),
                                sdf.format(timeWindow.getEnd()));
                        collector.collect(outData);

                    }
                })
                .print();

        env.execute("test");
    }
}
