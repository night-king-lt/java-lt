package effect.nio.tcp;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;

/**
 * @Author liu.teng
 * @Date 2020/8/13 20:40
 * @Version 1.0
 */
public class TcpChannel {
    private long endTime;
    private SelectionKey key;

    public TcpChannel(SelectableChannel channel, long endTime, int op) throws IOException
    {
        boolean done = false;
        Selector selector = null;
        this.endTime = endTime;
        try {
            selector = Selector.open();
            channel.configureBlocking(false);
            key = channel.register(selector, op);
            done = true;
        } finally {
            if (!done && selector != null) {
                selector.close();
            }
            if (!done) {
                channel.close();
            }
        }
    }

    static void blockUntil(SelectionKey key, long endTime) throws IOException
    {
        long timeout = endTime - System.currentTimeMillis();
        int nkeys = 0;
        if (timeout > 0) {
            nkeys = key.selector().select(timeout);
        } else if (timeout == 0) {
            nkeys = key.selector().selectNow();
        }
        if (nkeys == 0) {
            throw new SocketTimeoutException();
        }
    }

    void cleanup()
    {
        try {
            key.selector().close();
            key.channel().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    void bind(SocketAddress addr) throws IOException
    {
        SocketChannel channel = (SocketChannel) key.channel();
        channel.socket().bind(addr);
    }

    void connect(SocketAddress addr) throws IOException
    {
        SocketChannel channel = (SocketChannel) key.channel();
        if (channel.connect(addr))
            return;
        key.interestOps(SelectionKey.OP_CONNECT);
        try {
            while (!channel.finishConnect()) {
                if (!key.isConnectable()) {
                    blockUntil(key, endTime);
                }
            }
        } finally {
            if (key.isValid()) {
                key.interestOps(0);
            }
        }
    }

    void send(ByteBuffer buffer) throws IOException
    {
        Send.operate(key, buffer, endTime);
    }

    void recv(ByteBuffer buffer) throws IOException
    {
        Recv.operate(key, buffer, endTime);
    }
}

interface Operator
{
    class Operation
    {
        static void operate(final int op, final SelectionKey key, final ByteBuffer buffer, final long endTime, final Operator optr) throws IOException
        {
            final SocketChannel channel = (SocketChannel) key.channel();
            final int total = buffer.capacity();
            key.interestOps(op);
            try {
                while (buffer.position() < total) {
                    if (System.currentTimeMillis() > endTime) {
                        throw new SocketTimeoutException();
                    }
                    if ((key.readyOps() & op) != 0) {
                        if (optr.io(channel, buffer) < 0) {
                            throw new EOFException();
                        }
                    } else {
                        TcpChannel.blockUntil(key, endTime);
                    }
                }
            } finally {
                if (key.isValid()) {
                    key.interestOps(0);
                }
            }
        }
    }

    int io(SocketChannel channel, ByteBuffer buffer) throws IOException;
}
class Send implements Operator
{
    public int io(SocketChannel channel, ByteBuffer buffer) throws IOException
    {
        return channel.write(buffer);
    }
    public static final void operate(final SelectionKey key, final ByteBuffer buffer, final long endTime) throws IOException
    {
        Operation.operate(SelectionKey.OP_WRITE, key, buffer, endTime, operator);
    }
    public static final Send operator = new Send();
}

class Recv implements Operator
{
    public int io(SocketChannel channel, ByteBuffer buffer) throws IOException
    {
        return channel.read(buffer);
    }

    public static final void operate(final SelectionKey key, final ByteBuffer buffer, final long endTime) throws IOException
    {
        Operation.operate(SelectionKey.OP_READ, key, buffer, endTime, operator);
    }
    public static final Recv operator = new Recv();
}
