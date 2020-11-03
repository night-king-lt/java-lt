package flink.async;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.async.ResultFuture;
import org.apache.flink.streaming.api.functions.async.RichAsyncFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * @Author liu.teng
 * @Date 2020/11/3 11:18
 * @Version 1.0
 */
public class SampleAsyncFunction extends RichAsyncFunction<Integer, String> {
    private long[] sleep = {100L, 1000L, 5000L, 2000L, 6000L, 100L};

    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);
    }

    @Override
    public void close() throws Exception {
        super.close();
    }

    @Override
    public void asyncInvoke(final Integer input, final ResultFuture<String> resultFuture) {
        System.out.println(System.currentTimeMillis() + "-input:" + input + " will sleep " + sleep[input] + " ms");

        asyncQuery(input, resultFuture);
    }

    /**
     * 可以看到第一条数据进入到map算子的时间与最后一条相差了13115毫秒，执行的顺序与source中数据的顺序一致，并且是串行的。
     * @param input
     * @param resultFuture
     */
    private void query(final Integer input, final ResultFuture<String> resultFuture) {
        try {
            Thread.sleep(sleep[input]);
            resultFuture.complete(Collections.singletonList(String.valueOf(input)));
        } catch (InterruptedException e) {
            resultFuture.complete(new ArrayList<>(0));
        }
    }

    /**
     * 同样第一条数据进入map算子的时间与最后一条仅相差了6903毫秒，而且输出结果的顺序并不是source中的顺序，而是按照查询时间递增的顺序输出，并且查询请求几乎是同一时间发出的。
     * @param input
     * @param resultFuture
     */
    private void asyncQuery(final Integer input, final ResultFuture<String> resultFuture) {
        CompletableFuture.supplyAsync(new Supplier<Integer>() {

            @Override
            public Integer get() {
                try {
                    Thread.sleep(sleep[input]);
                    return input;
                } catch (Exception e) {
                    return null;
                }
            }
        }).thenAccept((Integer dbResult) -> {
            resultFuture.complete(Collections.singleton(String.valueOf(dbResult)));
        });
    }
}
