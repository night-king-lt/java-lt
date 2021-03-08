package flink.table;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/4
 */
public class TableTest10_v1 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings fsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, fsSettings);
        String show = "CREATE TABLE showTable (\n" +
                " eventTime bigint,\n" +
                " es AS TO_TIMESTAMP(FROM_UNIXTIME(eventTime / 1000)),\n" +
                " timeString string,\n" +
                " token string,\n" +
                " user_id string,\n" +
                " type int\n" +
                ") WITH (\n" +
                " 'update-mode' = 'append',\n" +
                " 'connector.version' = 'universal',\n" +
                " 'connector.type' = 'kafka',\n" +
                " 'connector.topic' = 'show',\n" +
                " 'connector.properties.zookeeper.connect' = 'localhost:2181',\n" +
                " 'connector.properties.bootstrap.servers' = 'localhost:9092',\n" +
                " 'connector.properties.group.id' = 'test-group',\n" +
                " 'connector.startup-mode' = 'latest-offset',\n" +
                " 'format.type' = 'csv'\n" +
                ")";
        tableEnv.sqlUpdate(show);

        Table table = tableEnv.sqlQuery("select * from showTable");
        DataStream<Row> result = tableEnv.toAppendStream(table, Row.class);
        result.print();

        env.execute("test");
    }
}
