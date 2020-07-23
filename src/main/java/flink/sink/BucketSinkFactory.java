package flink.sink;

import org.apache.flink.streaming.connectors.fs.bucketing.BucketingSink;
import org.apache.flink.streaming.connectors.fs.bucketing.DateTimeBucketer;

import java.time.ZoneId;

/**
 * @Author liuteng
 * @Date 2020/7/23 11:46 上午
 * @Version 1.0
 */
public class BucketSinkFactory {
    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd/HH/mm";

    /**
     *   gz压缩输出到hdfs
     * @param path
     * @return
     */
    public static BucketingSink<String> stringGzip(String path) {
        BucketingSink<String> sink = new BucketingSink<>(path);
        sink.setBucketer(new DateTimeBucketer<>(DEFAULT_DATE_FORMAT, ZoneId.of("Asia/Shanghai")));
        sink.setWriter(new GzipFileWriter<>());
        sink.setBatchSize(1024 * 1024 * 400);  // this is 400 MB
        sink.setBatchRolloverInterval(30 * 1000L);
        sink.setPartSuffix(".gz");
        return sink;
    }
}
