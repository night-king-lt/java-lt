package flink.iterations;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.IterativeStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.SourceFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *  斐不拉契数列
 */
public class FeiLaChi {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        int leng = 20;
        List<Tuple2<int[], Integer>> data = new ArrayList<>();
        int[] source = new int[leng];
        source[0] = 1;
        source[1] = 1;
        data.add(new Tuple2<>(source, 0));
        DataStream<Tuple2<int[], Integer>> dataSource = env.fromCollection(data);

        IterativeStream<Tuple2<int[], Integer>> iteration = dataSource.iterate();

        DataStream<Tuple2<int[], Integer>> runData = iteration.map(new MapFunction<Tuple2<int[], Integer>, Tuple2<int[], Integer>>() {
            @Override
            public Tuple2<int[], Integer> map(Tuple2<int[], Integer> value) throws Exception {
                int[] result = value.f0;
                int count = value.f1;
                int a0 = result[count++];
                int a1 = result[count];

                result[count + 1] = a0 + a1;
                return new Tuple2<>(result, count) ;
            }
        }).setParallelism(1);

        DataStream<Tuple2<int[], Integer>> stillData = runData.filter( v -> v.f1 < leng -2 ).setParallelism(1);
//        stillData.map( v -> Arrays.toString(v.f0) + "-" + v.f1).print();

        iteration.closeWith(stillData);

        DataStream<Tuple2<int[], Integer>> doneData = runData.filter( v -> v.f1 >= leng -2).setParallelism(1);

        doneData.map( v -> Arrays.toString(v.f0)).print();


        env.execute();
    }
}
