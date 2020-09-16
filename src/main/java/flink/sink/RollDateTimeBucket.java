package flink.sink;

import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.streaming.api.functions.sink.filesystem.BucketAssigner;
import org.apache.flink.streaming.api.functions.sink.filesystem.bucketassigners.SimpleVersionedStringSerializer;
import org.apache.flink.util.Preconditions;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @Author liu.teng
 * @Date 2020/9/15 21:09
 * @Version 1.0
 */
public class RollDateTimeBucket<IN>  implements BucketAssigner<IN, String> {

    private final ZoneId zoneId;
    private final String formatString;
    private final long rollInterval;
    private transient DateTimeFormatter dateTimeFormatter;

    public RollDateTimeBucket() {
        this("yyyy-MM-dd--HH", ZoneId.systemDefault(), 60L);
    }

    public RollDateTimeBucket(String formatString, long rollInterval) {
        this(formatString, ZoneId.systemDefault(), rollInterval);
    }

    public RollDateTimeBucket(String formatString, ZoneId zoneId, long rollInterval) {
        this.formatString = (String) Preconditions.checkNotNull(formatString);
        this.zoneId = (ZoneId)Preconditions.checkNotNull(zoneId);
        this.rollInterval = rollInterval;
        this.dateTimeFormatter = DateTimeFormatter.ofPattern(this.formatString).withZone(zoneId);
    }

    @Override
    public String getBucketId(IN in, Context context) {
        if (this.dateTimeFormatter == null) {
            this.dateTimeFormatter = DateTimeFormatter.ofPattern(this.formatString).withZone(this.zoneId);
        }
        System.out.println("rollInterval: " + rollInterval);
        long time = context.currentProcessingTime() / this.rollInterval * this.rollInterval;
        String buckedId = this.dateTimeFormatter.format(Instant.ofEpochMilli(time));
        System.out.println(buckedId);
        return buckedId;
    }

    @Override
    public SimpleVersionedSerializer<String> getSerializer() {
        return SimpleVersionedStringSerializer.INSTANCE;
    }

    public String toString() {
        return "DateTimeBucketAssigner{formatString='" + this.formatString + '\'' + ", zoneId=" + this.zoneId + '}';
    }
}
