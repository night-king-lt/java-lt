package flink.sink;

import flink.sink.factory.GzipFileWriterFactory;
import flink.source.StringSource;
import flink.source.Tuple2Source;
import org.apache.flink.api.common.serialization.SimpleStringEncoder;
import org.apache.flink.api.java.hadoop.mapred.utils.HadoopUtils;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.configuration.GlobalConfiguration;
import org.apache.flink.core.fs.Path;
import org.apache.flink.formats.sequencefile.SequenceFileWriterFactory;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.sink.filesystem.OutputFileConfig;
import org.apache.flink.streaming.api.functions.sink.filesystem.StreamingFileSink;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.DateTimeBucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.DefaultRollingPolicy;
import org.apache.flink.streaming.api.functions.sink.filesystem.rollingpolicies.OnCheckpointRollingPolicy;
import org.apache.flink.streaming.api.functions.source.SourceFunction;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.util.concurrent.TimeUnit;

/**
 * @Author liuteng
 * @Date 2020/7/22 11:55 上午
 * @Version 1.0
 */
public class StreamingFileSinkFactory {

    public static final String DEFAULT_DATE_FORMAT = "yyyyMMdd/HH/mm";

    public static StreamingFileSink<String> stringFile(String path){
        StreamingFileSink<String> result = StreamingFileSink.<String>forRowFormat(new Path(path), new SimpleStringEncoder<>("UTF-8"))
                .withBucketAssigner(new DateTimeBucketAssigner<>(DEFAULT_DATE_FORMAT))
                .withRollingPolicy(
                        DefaultRollingPolicy.builder()
                                .withRolloverInterval(TimeUnit.MINUTES.toMillis(4))
                                .withInactivityInterval(TimeUnit.MINUTES.toMillis(4))
                                .withMaxPartSize(1024 * 1024 * 1024) // 1G
                                .build())
                .build();
        return result;
    }

    public static StreamingFileSink<String> stringGzipFile(String path, long rollInterval){
        StreamingFileSink<String> result = StreamingFileSink.forBulkFormat(
                new Path(path),
                new GzipFileWriterFactory<String>())
                .withBucketAssigner(new RollDateTimeBucket<>(DEFAULT_DATE_FORMAT, rollInterval))
                .withOutputFileConfig(new OutputFileConfig("part", ".gz"))
                .build();
        return result;
    }

    public static StreamingFileSink<Tuple2<Text, BytesWritable>> seqFileGzip(String path){
        Configuration hadoopConf = HadoopUtils.getHadoopConfiguration(GlobalConfiguration.loadConfiguration());
        final StreamingFileSink<Tuple2<Text, BytesWritable>> sink = StreamingFileSink
                .<Tuple2<Text, BytesWritable>>forBulkFormat(
                        new Path(path),
                        new SequenceFileWriterFactory<>(hadoopConf, Text.class, BytesWritable.class, "org.apache.hadoop.io.compress.GzipCodec", SequenceFile.CompressionType.BLOCK)
                        )
                .withBucketAssigner(new DateTimeBucketAssigner<>(DEFAULT_DATE_FORMAT))
                .withRollingPolicy(OnCheckpointRollingPolicy.build())
                .build();
        return sink;
    }

    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.createLocalEnvironment();
        env.enableCheckpointing(300000);
        DataStreamSource<String> source = env.addSource(new SourceFunction<String>() {
            private static final long serialVersionUID = -1096914708441228503L;
            @Override
            public void run(SourceContext<String> sourceContext) throws Exception {
                while(true){
                    sourceContext.collect("flink-hdfs-test");
                }
            }
            @Override
            public void cancel() {
            }
        });

//        RollDateTimeBucket s = new RollDateTimeBucket<>(DEFAULT_DATE_FORMAT);
//        System.out.println(s);
//        System.out.println(s.getBucketId());

        StreamingFileSink<String> stringSink = StreamingFileSinkFactory.stringGzipFile("E:\\data\\flink", 300000);

        source.addSink(stringSink);

        env.execute();
    }
}
