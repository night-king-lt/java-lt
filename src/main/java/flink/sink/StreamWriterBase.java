package flink.sink;

import org.apache.flink.streaming.connectors.fs.Writer;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.io.IOException;

/**
 * @Author liuteng
 * @Date 2020/7/23 11:41 上午
 * @Version 1.0
 */
public abstract class StreamWriterBase <T> implements Writer<T> {
    private static final long serialVersionUID = 2L;
    public transient FSDataOutputStream outStream;
    private boolean syncOnFlush;

    public StreamWriterBase() {
    }

    protected StreamWriterBase(StreamWriterBase<T> other) {
        this.syncOnFlush = other.syncOnFlush;
    }

    public void setSyncOnFlush(boolean syncOnFlush) {
        this.syncOnFlush = syncOnFlush;
    }

    protected FSDataOutputStream getStream() {
        if (this.outStream == null) {
            throw new IllegalStateException("Output stream has not been opened");
        } else {
            return this.outStream;
        }
    }

    public void open(FileSystem fs, Path path) throws IOException {
        if (this.outStream != null) {
            throw new IllegalStateException("Writer has already been opened");
        } else {
            this.outStream = fs.create(path, false);
        }
    }

    public long flush() throws IOException {
        if (this.outStream == null) {
            throw new IllegalStateException("Writer is not open");
        } else {
            if (this.syncOnFlush) {
                this.outStream.hsync();
            } else {
                this.outStream.hflush();
            }

            return this.outStream.getPos();
        }
    }

    public long getPos() throws IOException {
        if (this.outStream == null) {
            throw new IllegalStateException("Writer is not open");
        } else {
            return this.outStream.getPos();
        }
    }

    public void close() throws IOException {
        if (this.outStream != null) {
            this.flush();
            this.outStream.close();
            this.outStream = null;
        }

    }

    public boolean isSyncOnFlush() {
        return this.syncOnFlush;
    }
}
