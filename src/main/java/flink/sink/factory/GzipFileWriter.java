package flink.sink.factory;

import org.apache.flink.api.common.serialization.BulkWriter;
import org.apache.hadoop.fs.FSDataOutputStream;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.GZIPOutputStream;

/**
 * @Author liu.teng
 * @Date 2020/9/15 17:03
 * @Version 1.0
 */
public class GzipFileWriter<T> implements BulkWriter<T> {

    private String charsetName;
    private transient Charset charset;
    private transient GZIPOutputStream gOut;

    public GzipFileWriter(FSDataOutputStream outStream) throws IOException {
        this("UTF-8", outStream);
    }

    public GzipFileWriter(String charsetName, FSDataOutputStream outStream) throws IOException {
        this.charsetName = charsetName;
        this.charset = Charset.forName(this.charsetName);
        gOut = new GZIPOutputStream(outStream);
    }

    @Override
    public void addElement(T t) throws IOException {
        this.gOut.write(t.toString().getBytes(this.charset));
        // 10 对应的 ASCII 是 换行
        this.gOut.write(10);
    }

    @Override
    public void flush() throws IOException {
        this.gOut.flush();
    }

    @Override
    public void finish() throws IOException {
        this.gOut.finish();
    }
}
