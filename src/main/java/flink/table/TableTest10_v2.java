package flink.table;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.bridge.java.StreamTableEnvironment;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/4
 */
public class TableTest10_v2 {

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

        String lable = "CREATE TABLE lable (\n" +
//                " eventTime bigint,\n" +
                " timeString string,\n" +
                " token string,\n" +
                " user_id string,\n" +
                " type int\n" +
                ") WITH (\n" +
                " 'update-mode' = 'append',\n" +
                " 'connector.version' = 'universal',\n" +
                " 'connector.type' = 'kafka',\n" +
                " 'connector.topic' = 'lab',\n" +
                " 'connector.properties.zookeeper.connect' = 'localhost:2181',\n" +
                " 'connector.properties.bootstrap.servers' = 'localhost:9092',\n" +
                " 'connector.properties.group.id' = 'test-group',\n" +
                " 'connector.startup-mode' = 'earliest-offset',\n" +
                " 'format.type' = 'csv'\n" +
                ")";
        tableEnv.sqlUpdate(lable);

        String joinSql =
                "        insert into lable" +
                "        select timeString, token, user_id, type from showTable";
        tableEnv.sqlUpdate(joinSql);

//        Table table = tableEnv.sqlQuery(joinSql);
//        Table table = tableEnv.sqlQuery("select * from lable");
//        DataStream<Row> result = tableEnv.toAppendStream(table, Row.class);
//        DataStream<Tuple2<Boolean, Row>> result = tableEnv.toRetractStream(table, Row.class);
//        result.print();

        env.execute("test");
    }
}
