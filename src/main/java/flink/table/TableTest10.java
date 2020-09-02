package flink.table;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/4
 */
public class TableTest10 {

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        EnvironmentSettings fsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, fsSettings);
        String show = "CREATE TABLE showTable (\n" +
                " eventTime bigint,\n" +
                " timeString string,\n" +
                " token string,\n" +
                " user_id string,\n" +
                " type int,\n" +
                " t AS TO_TIMESTAMP(FROM_UNIXTIME(eventTime / 1000))\n" +
                " ,WATERMARK FOR t AS t - INTERVAL '5' SECOND \n" +
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

        String click = "CREATE TABLE click (\n" +
                " eventTime bigint,\n" +
                " timeString string,\n" +
                " token string,\n" +
                " user_id string,\n" +
                " type int,\n" +
                " t AS TO_TIMESTAMP(FROM_UNIXTIME(eventTime / 1000))\n" +
                " ,WATERMARK FOR t AS t - INTERVAL '5' SECOND \n" +
                ") WITH (\n" +
                " 'update-mode' = 'append',\n" +
                " 'connector.version' = 'universal',\n" +
                " 'connector.type' = 'kafka',\n" +
                " 'connector.topic' = 'click',\n" +
                " 'connector.properties.zookeeper.connect' = 'localhost:2181',\n" +
                " 'connector.properties.bootstrap.servers' = 'localhost:9092',\n" +
                " 'connector.properties.group.id' = 'test-group',\n" +
                " 'connector.startup-mode' = 'latest-offset',\n" +
                " 'format.type' = 'csv'\n" +
                ")";
        tableEnv.sqlUpdate(click);

        String lable = "CREATE TABLE lable (\n" +
                " eventTime bigint,\n" +
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
//                "        insert into lable" +
                "        select d.eventTime, d.timeString, d.token, d.user_id ,d.type,e.type,   d.type + e.type\n" +
                "        from\n" +
                "            (\n" +
                "                SELECT t, token, timeString, user_id, eventTime, type\n" +
                "                FROM showTable\n" +
                "            ) d\n" +
                "        left join\n" +
                "            (\n" +
                "                SELECT t, token, user_id, eventTime, type\n" +
                "                FROM click\n" +
                "            ) e\n" +
                "        on d.user_id = e.user_id\n" +
                "        AND  d.token = e.token "
                        + "AND  (d.t BETWEEN (e.t - INTERVAL '2' SECOND) AND  (e.t + INTERVAL '2' SECOND))";
//        tableEnv.sqlUpdate(joinSql);

//        Table table = tableEnv.sqlQuery(joinSql);
        Table table = tableEnv.sqlQuery(joinSql);
        System.out.println(tableEnv.explain(table));
//        DataStream<Row> result = tableEnv.toAppendStream(table, Row.class);
        DataStream<Tuple2<Boolean, Row>> result = tableEnv.toRetractStream(table, Row.class);
        result.print();

        env.execute("test");
    }
}
