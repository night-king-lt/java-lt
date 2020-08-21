package effect.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.MessageDigest;
import java.util.Iterator;

/**
 * @Author liu.teng
 * @Date 2020/8/13 20:42
 * @Version 1.0
 */
public class Server {
    /**
     * 服务端通信范例程序主函数
     *
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException
    {
        // Create the selector
        final Selector selector = Selector.open();
        final ServerSocketChannel server = ServerSocketChannel.open();
        server.configureBlocking(false);
        server.socket().bind(new InetSocketAddress("xx.xx.xx.xx", 5656), 5);
        // Register both channels with selector
        server.register(selector, SelectionKey.OP_ACCEPT);
        new Thread(new Daemon(selector)).start();
    }
}

class Daemon implements Runnable
{
    private final Selector selector;

    Daemon(Selector selector)
    {
        this.selector = selector;
    }

    public void run()
    {
        while (true) {
            try {
                // Wait for an event
                selector.select();

                // Get list of selection keys with pending events
                Iterator<SelectionKey> it = selector.selectedKeys().iterator();

                // Process each key
                while (it.hasNext()) {
                    // Get the selection key
                    SelectionKey selKey = it.next();

                    // Remove it from the list to indicate that it is being processed
                    it.remove();

                    // Check if it's a connection request
                    if (selKey.isAcceptable()) {
                        // Get channel with connection request
                        ServerSocketChannel server = (ServerSocketChannel) selKey.channel();
                        // Accept the connection request.
                        // If serverSocketChannel is blocking, this method blocks.
                        // The returned channel is in blocking mode.
                        SocketChannel channel = server.accept();

                        // If serverSocketChannel is non-blocking, sChannel may be null
                        if (channel != null) {
                            // Use the socket channel to communicate with the client
                            new Thread(new ServerHandler(channel)).start();
                        } else {
                            System.out.println("---No Connection---");
                            // There were no pending connection requests; try again later.
                            // To be notified of connection requests,
                        }
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}

class ServerHandler implements Runnable
{
    private static final long timeout = 30 * 1000; // 设置超时时间为30秒
    private static int counter = 0;
    private final TcpChannel channel;
    private final MessageDigest md;

    ServerHandler(SocketChannel channel) throws Exception
    {
        this.channel = new TcpChannel(channel, System.currentTimeMillis() + timeout, SelectionKey.OP_READ);
        md = MessageDigest.getInstance("md5");
    }

    public void run()
    {
        try {
            while (true) {
                work();
                synchronized (ServerHandler.class) {
                    if ((++counter & 65535) == 0) {
                        System.out.println(counter);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.cleanup();
        }
    }

    private void work() throws IOException
    { // 模拟工作流程
        byte[] cache = new byte[256], reply = new byte[5];
        read(cache, reply);
    }

    private void read(byte[] cache, byte[] reply) throws IOException
    { // 从套接字读入数据
        channel.recv(ByteBuffer.wrap(cache));
        md.reset();
        md.update(cache, 0, 240);
        byte[] md5 = md.digest(); // 使用前240字节产生MD5校验码
        if (!ExtArrays.partialEquals(md5, 0, cache, 240, 16)) { // 与后16字节比较
            reply[0] = '?';
            System.out.println("MISMATCH!");
        } else {
            reply[0] = '.';
        }
        channel.send(ByteBuffer.wrap(reply)); // 返回接收结果
    }
}

final class ExtArrays
{
    private ExtArrays()
    {
    }

    public static boolean partialEquals(byte[] a, int offset_a, byte[] b, int offset_b, int len)
    { // 字节数组的部分比较
        if (a == null || b == null) {
            return false;
        }
        if (offset_a + len > a.length || offset_b + len > b.length) {
            return false;
        }
        for (int i = offset_a, j = offset_b, k = len; k > 0; i++, j++, k--) {
            if (a[i] != b[j]) {
                return false;
            }
        }
        return true;
    }
}
