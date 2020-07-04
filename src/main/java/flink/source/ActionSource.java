package flink.source;

import model.ActionData;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.RichSourceFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/3
 */
public class ActionSource {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<ActionData> dataStream = env.addSource(new ActionSource.innerSource());
        dataStream.print();

        env.execute();
    }

    public static class innerSource extends RichSourceFunction<ActionData> {

        static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        final String[] action = new String[]{"show", "click"};
        final String[] users = new String[]{"郭德纲", "于谦", "高峰"};

        ExecutorService poll;
        ArrayBlockingQueue<ActionData> queue;
        Runnable runnable;


        @Override
        public void open(Configuration parameters) throws Exception{
            queue = new ArrayBlockingQueue<>(5);
            poll = Executors.newFixedThreadPool(1);
            runnable = () -> {
                try {
                    for (int i=0; i<2; i ++){
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
                        Thread.sleep(50000);
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
    }
}
