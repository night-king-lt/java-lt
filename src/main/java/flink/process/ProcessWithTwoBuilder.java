package flink.process;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.ProcessFunction;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.flink.util.Collector;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/6/28
 *
 *   process类 如果包含两个构造器，并且其中一个调用了另一个构造器，那么在 processElement 方法中 获取不到当时 process类
 *
 */
public class ProcessWithTwoBuilder {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        DataStream<String> dataStream = env.addSource(new SourceFunction<String>() {
            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                sourceContext.collect("flink");
            }

            @Override
            public void cancel() {

            }
        });

        dataStream.process(new OneBuilder("one"));
        dataStream.process(new Two2Builder("two2"));
        dataStream.process(new Two2Builder("two2", 25));

        env.execute();

    }

    public static class OneBuilder extends ProcessFunction<String, String>{

        private String name;

        OneBuilder(String name){
            this.name = name;
        }

        @Override
        public void processElement(String s, Context context, Collector<String> collector) throws Exception {
            System.out.println(s + ": " + name);
        }
    }

    public static class Two2Builder extends ProcessFunction<String, String>{

        private String name;
        private int age;

        Two2Builder(String name){
            this(name, 20);
        }

        Two2Builder(String name, int age){
            this.name = name;
            this.age = age;
        }

        @Override
        public void processElement(String s, Context context, Collector<String> collector) throws Exception {
            System.out.println(s + ": " + name);
            System.out.println(this);
        }
    }

    public static class TwoBuilder extends ProcessFunction<String, String>{

        private String name;
        private int age;

        TwoBuilder(String name){
            setFun(name, 20);
        }

        TwoBuilder(String name, int age){
            setFun(name, age);
        }

        public void setFun(String name, int age){
            this.name = name;
            this.age = age;
        }

        @Override
        public void processElement(String s, Context context, Collector<String> collector) throws Exception {
            System.out.println(s + ": " + name);
            System.out.println(this);
        }
    }
}
