package flink.sink;

import org.apache.flink.streaming.connectors.fs.Writer;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.IllegalCharsetNameException;
import java.nio.charset.UnsupportedCharsetException;
import java.util.zip.GZIPOutputStream;

/**
 * @Author liuteng
 * @Date 2020/7/23 11:40 上午
 * @Version 1.0
 *
 *      flink sink hdfs   gzip 压缩
 */
public class GzipFileWriter<T> extends StreamWriterBase<T> {
    private static final long serialVersionUID = -5554862097412327537L;
    private String charsetName;
    private transient Charset charset;
    private transient GZIPOutputStream gOut;
    public GzipFileWriter() {
        this("UTF-8");
    }

    public GzipFileWriter(String charsetName) {
        this.charsetName = charsetName;
    }

    protected GzipFileWriter(GzipFileWriter<T> other) {
        super(other);
        this.charsetName = other.charsetName;
        this.charset = other.charset;
        this.gOut = other.gOut;
    }

    @Override
    public void open(FileSystem fs, Path path) throws IOException {
        super.open(fs, path);
        try {
            this.charset = Charset.forName(this.charsetName);
            gOut = new GZIPOutputStream(this.getStream());
        } catch (IllegalCharsetNameException var4) {
            throw new IOException("The charset " + this.charsetName + " is not valid.", var4);
        } catch (UnsupportedCharsetException var5) {
            throw new IOException("The charset " + this.charsetName + " is not supported.", var5);
        }
    }

    @Override
    public void write(T t) throws IOException {
        this.gOut.write(t.toString().getBytes(this.charset));
        // 10 对应的 ASCII 是 换行
        this.gOut.write(10);
    }

    @Override
    public void close() throws IOException {
        if (this.gOut != null){
            super.flush();
            this.gOut.close();
            super.outStream = null;
        }
    }



    @Override
    public Writer<T> duplicate() {
        return new GzipFileWriter<>(this);
    }
}
