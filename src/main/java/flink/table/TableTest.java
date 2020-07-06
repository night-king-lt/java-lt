package flink.table;
import flink.source.Tuple2Source;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/4
 */
public class TableTest {

    public static void main(String[] args) throws Exception {
        EnvironmentSettings fsSettings = EnvironmentSettings.newInstance().useOldPlanner().inStreamingMode().build();
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        StreamTableEnvironment fsTableEnv = StreamTableEnvironment.create(env, fsSettings);

//        DataStream<Tuple2<String, Integer>> source = env.addSource(new Tuple2Source.innerSource());
//        Table table = fsTableEnv.fromDataStream(source);
//
//        table.distinct().printSchema();

        env.execute();
    }
}
