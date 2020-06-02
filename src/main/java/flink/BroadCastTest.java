package flink;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.*;

public class BroadCastTest {
    public static void main(String[] args) {

        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        Map<String, Integer>  map = new HashMap<>();
        map.put("a", 100);
        map.put("b", 100);

        DataStream<Tuple2<String, Integer>> dataStream = env
                .socketTextStream("localhost", 9000)
                .flatMap(new WordCountJava.Splitter())
                .keyBy(0)
                .timeWindow(Time.seconds(5))
                .sum(1);

        dataStream.map(new MapFunction<Tuple2<String, Integer>, Object>() {
            @Override
            public Object map(Tuple2<String, Integer> v) throws Exception {
                if (map.containsKey(v.f0)) {
                    int a = v.f1 + map.get(v.f0);
                    return new Tuple2<>(v.f0, a);
                } else {
                    return v;
                }
            }
        }).print();
        try {
            env.execute("Window flink.WordCount");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
