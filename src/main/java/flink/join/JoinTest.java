package flink.join;

import flink.source.ActionSource;
import flink.watermark.MyWatermark;
import model.ActionData;
import org.apache.flink.api.common.typeinfo.BasicTypeInfo;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.PojoTypeInfo;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.SingleOutputStreamOperator;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
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
 *     interval join :  A join B   between -50s 50s  A流的每条数据join前后50s的 B 流数据
 *     前提： 必须使用 eventTime 设置水印
 */
public class JoinTest {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        OutputTag<ActionData> clickOutputTag = new OutputTag<>("click", TypeInformation.of(ActionData.class));
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);
        //定义数据源
        DataStream<ActionData> source = env.addSource(new ActionSource()).assignTimestampsAndWatermarks(new MyWatermark.innerWatermark());

        SingleOutputStreamOperator<ActionData> show = source.process(new ProcessFunction<ActionData, ActionData>() {

            @Override
            public void processElement(ActionData actionData, Context context, Collector<ActionData> collector) throws Exception {
                if (actionData.getShow()){
                    collector.collect(actionData);
                }else {
                    context.output(clickOutputTag, actionData);
                }
            }
        });
        DataStream<ActionData> click = show.getSideOutput(clickOutputTag);

        show.keyBy(ActionData::getUserId)
            .intervalJoin(click.keyBy(ActionData::getUserId))
            .between(Time.seconds(-50), Time.seconds(50))
            .process(new ProcessJoinFunction<ActionData, ActionData, Object>() {
                @Override
                public void processElement(ActionData actionData, ActionData actionData2, Context context, Collector<Object> collector) throws Exception {
                    System.out.println("show: " + actionData);
                    System.out.println("click: " + actionData2);
                }
            });


        env.execute();
    }

    public static class ActionSource implements SourceFunction<ActionData>{
        static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        @Override
        public void run(SourceContext<ActionData> sourceContext) throws Exception {
            String user = "flink";

            ActionData show = new ActionData();
            Date now = new Date();
            show.setEventTime(now.getTime());
            show.setTimeString(simpleDateFormat.format(now));
            show.setShow(true);
            show.setUserId(user);
            sourceContext.collect(show);

            Thread.sleep(40000);
            ActionData click = new ActionData();
            Date now2 = new Date();
            click.setEventTime(now2.getTime());
            click.setTimeString(simpleDateFormat.format(now2));
            click.setUserId(user);
            click.setClick(true);
            sourceContext.collect(click);
        }

        @Override
        public void cancel() {

        }
    }
}
