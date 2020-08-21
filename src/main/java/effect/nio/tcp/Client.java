package effect.nio.tcp;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.security.DigestException;
import java.security.MessageDigest;
import java.util.Random;
/**
 * @Author liu.teng
 * @Date 2020/8/13 20:42
 * @Version 1.0
 */
public class Client {
    private static int id = 0;
    /**
     * 客户端通信范例程序主函数
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        new Thread(new ClientHandler(id++)).start();
        new Thread(new ClientHandler(id++)).start();
        new Thread(new ClientHandler(id++)).start();
        new Thread(new ClientHandler(id++)).start();
        new Thread(new ClientHandler(id++)).start();
    }

}

class ClientHandler implements Runnable
{
    private static final long timeout = 30 * 1000; // 设置超时时间为30秒
    private final TcpChannel channel;

    private final int id;

    private final MessageDigest md;
    private final Random rand;

    ClientHandler(int id) throws Exception
    {
        this.id = id;
        channel = new TcpChannel(SocketChannel.open(), System.currentTimeMillis() + timeout, SelectionKey.OP_WRITE);
        md = MessageDigest.getInstance("md5");
        rand = new Random();
    }

    @Override
    public void run()
    {
        try {
            channel.connect(new InetSocketAddress("xx.xx.xx.xx", 5656));
            int i = 0;
            while (true) {
                work();
                if ((++i & 16383) == 0) {
                    System.out.println(String.format("client(%1$d): %2$d", id, i));
                }
                Thread.yield();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            channel.cleanup();
        }
    }

    private void work() throws IOException, DigestException
    {
        byte[] cache = new byte[256], reply = new byte[5];
        write(cache, reply);
    }

    private void write(byte[] cache, byte[] reply) throws DigestException, IOException
    {
        rand.nextBytes(cache); // 只用前面的240字节
        md.reset();
        md.update(cache, 0, 240);
        md.digest(cache, 240, 16); // MD5校验码占后面16字节
        ByteBuffer buffer = ByteBuffer.wrap(cache);
        channel.send(buffer);
        buffer = ByteBuffer.wrap(reply);
        channel.recv(buffer);
        if (reply[0] != '.') { // 若接收的结果不正确，可以考虑尝试再次发送
            System.out.println("MISMATCH!");
        }
    }
}
