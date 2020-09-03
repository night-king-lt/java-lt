package flink.join;

import flink.watermark.MyWatermark;
import model.ActionData;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.KeyedProcessFunction;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.co.ProcessJoinFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;
import org.apache.flink.util.OutputTag;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/3
 *
 *     KeyedProcessFunction 实现 曝光点击跨窗口拼接
 */
public class TimeJoin {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        OutputTag<ActionData> clickOutputTag = new OutputTag<>("click", TypeInformation.of(ActionData.class));
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //定义数据源
        DataStream<ActionData> source = env.addSource(new ActionSource()).assignTimestampsAndWatermarks(new MyWatermark.innerWatermark());

        SingleOutputStreamOperator<ActionData> show = source.process(new ProcessFunction<ActionData, ActionData>() {

            @Override
            public void processElement(ActionData actionData, Context context, Collector<ActionData> collector) throws Exception {
                if (actionData.getType() == 1){
                    collector.collect(actionData);
                }else {
                    context.output(clickOutputTag, actionData);
                }
            }
        });
        DataStream<ActionData> click = show.getSideOutput(clickOutputTag);

        show.union(click)
            .keyBy(ActionData::getUserId)
            .process(new LableJoinProcess(5000L))
        .print();

        env.execute();
    }

    public static class LableJoinProcess extends KeyedProcessFunction<String, ActionData, ActionData> {

        //  曝光，点击状态保存
        private ValueState<ActionData> showState;
        private ValueState<ActionData> clickState;
        private long  timerDelay;

        public LableJoinProcess(long timerDelay) {
            this.timerDelay = timerDelay;
        }

        @Override
        public void open(Configuration parameters) {
            // 获取状态
            showState = getRuntimeContext().getState(new ValueStateDescriptor<>("show_state", ActionData.class));
            clickState = getRuntimeContext().getState(new ValueStateDescriptor<>("click_state", ActionData.class));
        }

        @Override
        public void processElement(ActionData value, Context ctx,
                                   Collector<ActionData> out) throws Exception {
            long time = System.currentTimeMillis() + this.timerDelay;
            // 注册时间触发器，到时间就会调用 onTimer 方法  每条数据都会在 time延时后调用onTime方法  除非在这之前 时间触发器被删除
            ctx.timerService().registerProcessingTimeTimer(time);

            if (value.getType() == 2) {
                // 更新点击状态
                clickState.update(value);
            }else {
                // 更新曝光状态
                showState.update(value);
            }
            if (showState.value() != null && clickState.value() != null){
                // 如果拼接上了，删除 time 时刻的时间触发器
                ctx.timerService().deleteProcessingTimeTimer(time);
                // 更新状态
                ActionData lable = clickState.value();
                lable.setType(showState.value().getType() + lable.getType());
                // 向下游发送拼接上的数据
                out.collect(lable);
                // 清空曝光点击状态
                showState.clear();
                clickState.clear();
            }

        }

        @Override
        public void onTimer(long timestamp, OnTimerContext ctx, Collector<ActionData> out) throws Exception {
            if (showState.value() != null){
                // 向下游发送 曝光数据（拼不上点击）
                out.collect(showState.value());
                showState.clear();
            }
            if (clickState.value() != null){
                // 向下游发送 点击数据（拼不上曝光）
                out.collect(clickState.value());
                clickState.clear();
            }

        }
    }

    public static class ActionSource implements SourceFunction<ActionData>{
        static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        @Override
        public void run(SourceContext<ActionData> sourceContext) throws Exception {
            ActionData show = new ActionData();
            show.setEventTime(new Date().getTime());
            show.setTimeString(simpleDateFormat.format(new Date()));
            show.setType(1);
            show.setUserId("flink");
            sourceContext.collect(show);

            ActionData showCopy = new ActionData();
            showCopy.setEventTime(new Date().getTime());
            showCopy.setTimeString(simpleDateFormat.format( new Date()));
            showCopy.setUserId("flink");
            showCopy.setType(1);
            sourceContext.collect(showCopy);

            Thread.sleep(4000);
            ActionData click = new ActionData();
            click.setEventTime(new Date().getTime());
            click.setTimeString(simpleDateFormat.format( new Date()));
            click.setUserId("flink");
            click.setType(2);
            sourceContext.collect(click);

            ActionData show1 = new ActionData();
            show1.setEventTime(new Date().getTime());
            show1.setTimeString(simpleDateFormat.format(new Date()));
            show1.setType(1);
            show1.setUserId("java");
            sourceContext.collect(show1);

            Thread.sleep(11000);
            ActionData click1 = new ActionData();
            click1.setEventTime(new Date().getTime());
            click1.setTimeString(simpleDateFormat.format(new Date()));
            click1.setUserId("hadoop");
            click1.setType(2);
            sourceContext.collect(click1);

            while(true){

            }
        }

        @Override
        public void cancel() {

        }
    }
}
