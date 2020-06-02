package flink.window;

import dao.ActionData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.api.common.functions.ReduceFunction;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AssignerWithPeriodicWatermarks;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.functions.windowing.ProcessWindowFunction;
import org.apache.flink.streaming.api.watermark.Watermark;
import org.apache.flink.streaming.api.windowing.assigners.TumblingEventTimeWindows;
import org.apache.flink.streaming.api.windowing.assigners.TumblingProcessingTimeWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.streaming.api.windowing.windows.Window;
import org.apache.flink.util.Collector;

import javax.annotation.Nullable;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class windowDemo {

    static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

    public static void main(String[] args) throws Exception {

        final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        //定义数据源
        DataStream<ActionData> source = env.addSource(new RichSourceFunction<ActionData>() {
            final String[] action = new String[]{"show", "click"};
            final String[] users = new String[]{"郭德纲", "于谦", "高峰"};

            ExecutorService poll;
            ArrayBlockingQueue<ActionData> queue;
            Runnable runnable;


            @Override
            public void open(Configuration parameters) throws Exception{
                queue = new ArrayBlockingQueue<>(5);
                poll = Executors.newFixedThreadPool(3);
                runnable = () -> {
                    try {
                        for (int i=0; i<10; i ++){
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
            public void run(SourceContext<ActionData> sourceContext) throws Exception {
                while (true){
                    ActionData actionData = queue.take();
                    Date now = new Date();
                    String currentTime = simpleDateFormat.format(now);
                    if (actionData.getShow()){
                        actionData.setShowTime(currentTime);
                    }else{
                        actionData.setClickTime(currentTime);
                    }

                    sourceContext.collect(actionData);
                }
            }
            // 生产数据
            public List<ActionData> createData(){
                List<ActionData> result = new ArrayList<>();
//                int index = RandomUtils.nextInt(0,2);
                int index = 2;
                String token = RandomStringUtils.randomAlphabetic(5);
                String user = users[RandomUtils.nextInt(0,3)];
                for (int i=0; i<index; i++){
                    ActionData actionData = new ActionData();
                    Date now = new Date();
                    String type = action[i];
                    actionData.setEventTime(now.getTime());
                    actionData.setTimeString(simpleDateFormat.format(now));
                    actionData.setToken(token);
                    actionData.setUserId(user);
                    if (type.equals("show")){
                        actionData.setShow(true);
                    }else{
                        actionData.setClick(true);
                    }
                    result.add(actionData);
                }

                return result;
            }
            // 随机延时加入队列
            public void addQueue(List<ActionData> dataList) throws InterruptedException {
                if (dataList.size() == 1){
                    // 只有曝光，直接发送
                    queue.put(dataList.get(0));
                }else{
                    // 有点击行为，那么点击随机延时发送
                    dataList.forEach(x -> {
                        try {
                            Thread.sleep(6000);
                            queue.put(x);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }

            @Override
            public void cancel() {

            }
        }).assignTimestampsAndWatermarks(new AssignerWithPeriodicWatermarks<ActionData>() {
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
                System.out.println("watermark: " + simpleDateFormat.format(getCurrentWatermark().getTimestamp()) + " currmax: " + simpleDateFormat.format(currentMaxTimestamp) );
                return currentMaxTimestamp;
            }
        });

//        source.print();

        source.keyBy(x -> x.getToken() + x.getUserId())
                .window(TumblingEventTimeWindows.of(Time.seconds(5)))
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
