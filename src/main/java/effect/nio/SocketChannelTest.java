package effect.nio;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.sql.Time;
import java.util.Iterator;

/**
 * @Author liu.teng
 * @Date 2020/8/13 20:04
 * @Version 1.0
 */
public class SocketChannelTest {

    private static final int port = 30008;
    private static final int TIMEOUT = 3000;
    private static byte end = '\n';

    public static void main(String[] args) throws Exception {
        // TODO Auto-generated method stub
        Thread serverThread = new ServerThread();
        serverThread.start();
        Thread clientThread = new ClientThread();
        clientThread.start();
        Thread.sleep(2000);
    }

    static class ServerThread extends Thread{
        @Override
        public void run() {
            ServerSocketChannel channel = null;
            try{
                channel = ServerSocketChannel.open();
                SocketAddress address = new InetSocketAddress(port);
                channel.configureBlocking(false); // 设置非阻塞模式
                ServerSocket socket = channel.socket();
                socket.setReuseAddress(true);
                socket.bind(address);
                Selector selector = Selector.open();
                channel.register(selector, SelectionKey.OP_ACCEPT);
                while(selector.isOpen()){
                    if (selector.select(TIMEOUT) == 0){
                        System.out.print("...");
                        continue;
                    }
                    Iterator<SelectionKey> it = selector.selectedKeys().iterator();
                    while(it.hasNext()){
                        SelectionKey key = it.next();
                        if (!key.isValid()){
                            key.channel().close();
                            continue;
                        }
                        if (key.isAcceptable()){
                            this.accept(key);
                        }else if (key.isReadable()){
                            this.read(key);
                        }else if (key.isWritable()){
                            this.write(key);
                        }
                        it.remove();
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if (channel != null){
                    try{
                       channel.close();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }

        public void accept(SelectionKey key) throws Exception{
            SocketChannel socketChannel = ((ServerSocketChannel) key.channel()).accept();
            socketChannel.configureBlocking(false);
            socketChannel.register(key.selector(), SelectionKey.OP_READ);
            // 获取连接后,开始准备接受client的write,即当前socket的read.
            System.out.println("Server accept...");
        }

        public void read(SelectionKey key) throws Exception{
            System.out.println("Server reads begin..");
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer buffer = ByteBuffer.allocate(1024);
            while(socketChannel.read(buffer) != -1){
                byte last = buffer.get(buffer.position() - 1);
                System.out.println("l:" + buffer.position() + "//" + last + "//" + end);
                if(last == end){
                    break;
                }
                Thread.sleep(500);
            }
            buffer.flip();
            Charset charset = Charset.defaultCharset();
            CharBuffer charBuffer = charset.decode(buffer);
            System.out.println("Server read:" + charBuffer.toString());
            key.interestOps(SelectionKey.OP_WRITE);
        }

        public void write(SelectionKey key) throws Exception{
            SocketChannel socketChannel = (SocketChannel) key.channel();
            Charset charset = Charset.defaultCharset();
            String data = "S : " + System.currentTimeMillis();
            ByteBuffer byteBuffer = charset.encode(data);
            int limit = byteBuffer.limit();
            byteBuffer.clear();
            byteBuffer.position(limit);
            byteBuffer.put(end);
            byteBuffer.flip();
            while(byteBuffer.hasRemaining()){
                socketChannel.write(byteBuffer);
            }
            key.interestOps(SelectionKey.OP_READ);
        }
    }

    static class ClientThread extends Thread {
        @Override
        public void run() {
            SocketChannel channel = null;
            try {
                channel = SocketChannel.open();
                channel.configureBlocking(false);
                boolean isSuccess = channel.connect(new InetSocketAddress(port));
                if(!isSuccess){
                    while (!channel.finishConnect()) {
                        System.out.println("Connecting...");
                        Thread.sleep(1000);
                    }
                }
                Selector selector = Selector.open();
                channel.register(selector, SelectionKey.OP_WRITE);
                while (selector.isOpen()) {
                    if (selector.select(TIMEOUT) == 0) {
                        System.out.println("C...");
                        continue;
                    }
                    Iterator<SelectionKey> it = selector.selectedKeys()
                            .iterator();
                    while (it.hasNext()) {
                        SelectionKey key = it.next();
                        if (!key.isValid()) {
                            channel.close();
                            return;
                        }
                        if (key.isWritable()) {
                            write(key);
                        } else if (key.isReadable()) {
                            read(key);
                        }
                        it.remove();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }finally{
                if(channel != null){
                    try{
                        channel.close();
                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                }
            }
        }

        private void write(SelectionKey key) throws Exception {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            Charset charset = Charset.defaultCharset();
            String data = "C : " + System.currentTimeMillis();
            ByteBuffer byteBuffer = charset.encode(data);
            int limit = byteBuffer.limit();
            byteBuffer.clear();
            byteBuffer.position(limit);
            byteBuffer.put(end);
            byteBuffer.flip();
            while(byteBuffer.hasRemaining()){
                socketChannel.write(byteBuffer);
            }
            System.out.println("Client has writeen!");
            key.interestOps(SelectionKey.OP_READ);
        }

        private void read(SelectionKey key) throws Exception {
            SocketChannel socketChannel = (SocketChannel) key.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            while(socketChannel.read(byteBuffer) != -1){
                byte last = byteBuffer.get(byteBuffer.position()-1);
                if(last == end){
                    break;
                }
            }
            byteBuffer.flip();
            Charset charset = Charset.defaultCharset();
            CharBuffer charBuffer = charset.decode(byteBuffer);
            System.out.println("Client read: " + charBuffer.toString());
            key.interestOps(SelectionKey.OP_WRITE);
        }
    }
}
