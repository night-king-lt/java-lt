package flink.table;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.table.api.EnvironmentSettings;
import org.apache.flink.table.api.Table;
import org.apache.flink.table.api.TableEnvironment;
import org.apache.flink.table.api.java.StreamTableEnvironment;
import org.apache.flink.types.Row;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/4
 */
public class TableTest11 {

    public static void main(String[] args) throws Exception {
//        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironmentWithWebUI(new Configuration());
//        EnvironmentSettings bsSettings = EnvironmentSettings.newInstance().useBlinkPlanner().inStreamingMode().build();
//        StreamTableEnvironment tableEnv = StreamTableEnvironment.create(env, bsSettings);
//        String createTable11 = "CREATE TABLE kafkaTable (\n" +
//                "    user_id string,\n" +
//                "    countV bigint\n" +
//                ") WITH (\n" +
//                "    'connector' = 'kafka',\n" +
//                "    'topic' = 'test',\n" +
//                "    'properties.group.id' = 'testGroup" + "',\n" +
//                "    'properties.bootstrap.servers' = 'localhost:9092',\n" +
//                "    'format' = 'csv'\n" +
//                ")";
//        tableEnv.executeSql(createTable11);
//
//        Table table = tableEnv.sqlQuery("SELECT user_id, countV FROM kafkaTable ");
//        DataStream<Row> result = tableEnv.toAppendStream(table, Row.class);
//        result.print();
//
//        env.execute("test");
    }
}
