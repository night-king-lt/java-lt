package flink.sink.factory;

import org.apache.flink.api.common.serialization.BulkWriter;
import org.apache.flink.api.common.serialization.BulkWriter.Factory;
import org.apache.flink.core.fs.FSDataOutputStream;

import java.io.IOException;

/**
 * @Author liu.teng
 * @Date 2020/9/15 17:36
 * @Version 1.0
 */
public class GzipFileWriterFactory<T> implements Factory<T> {
    @Override
    public BulkWriter<T> create(FSDataOutputStream fs) throws IOException {
        org.apache.hadoop.fs.FSDataOutputStream stream = new org.apache.hadoop.fs.FSDataOutputStream(fs, null);
        return new GzipFileWriter<>(stream);
    }
}
