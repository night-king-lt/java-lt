package flink.window;

import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WindowTest {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        //定义数据源
        DataStream<Tuple2<String, Integer>> source = env.addSource(new RichSourceFunction<Tuple2<String, Integer>>() {

            ExecutorService poll;
            ArrayBlockingQueue<Tuple2<String, Integer>> queue;
            Runnable runnable;

            @Override
            public void open(Configuration parameters) throws Exception {
                queue = new ArrayBlockingQueue<>(5);
                poll = Executors.newFixedThreadPool(3);
                runnable = () -> {
                    try {
                        for (int i = 0; i < 1; i++) {
                            addQueue(createData());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                };
                for (int i = 0; i < 1; i++) {
                    poll.submit(runnable);
                }
                poll.shutdown();
            }

            @Override
            public void run(SourceContext<Tuple2<String, Integer>> sourceContext) throws Exception {
                while (true) {
                    Tuple2<String, Integer> actionData = queue.take();
                    sourceContext.collect(actionData);
                }
            }

            @Override
            public void cancel() {

            }

            // 生产数据
            public List<Tuple2<String, Integer>> createData() {
                List<Tuple2<String, Integer>> result = new ArrayList<>();
                result.add(new Tuple2<>("a", 1));
                result.add(new Tuple2<>("a", 1));
                result.add(new Tuple2<>("b", 1));
                result.add(new Tuple2<>("c", 1));
                result.add(new Tuple2<>("d", 1));
                result.add(new Tuple2<>("e", 1));
                result.add(new Tuple2<>("f", 1));
                return result;
            }

            // 随机延时加入队列
            public void addQueue(List<Tuple2<String, Integer>> dataList) throws InterruptedException {

                dataList.forEach(x -> {
                    try {
//                        Thread.sleep(6000);
                        queue.put(x);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
            }
        });

        source.keyBy(x -> x.f0)
                .window(TumblingProcessingTimeWindows.of(Time.seconds(10)))
                .reduce((ReduceFunction<Tuple2<String, Integer>>) (t1, t2) -> {
                    System.out.println(t1.f1);
                    t1.f1 = t1.f1 + t2.f1;
                    return t1;
                 }).print();

        env.execute("window test");

    }
}
