package effect.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @Author liu.teng
 * @Date 2020/8/12 10:30
 * @Version 1.0
 *   address: http://ifeve.com/buffers/
 */
public class FileChannelTest {

    public static void main(String[] args) {
        String p = "src/main/resources/wordCount.txt";
        methodNio(p);
    }

    public static void methodNio(String p){
        try(FileInputStream in = new FileInputStream(new File(p))){
            FileChannel fc = in.getChannel();
            // 分配一个容量为200字节的buf
            ByteBuffer buf = ByteBuffer.allocate(200);
            System.out.println(buf.limit());
            int len;

            // 从Channel将数据读到Buffer
            while( (len = fc.read(buf)) != -1){
                buf.flip();  // 设置成读模式
                byte[] bytes = new byte[len];
                buf.get(bytes);
                System.out.println(new String(bytes));
            }
            System.out.println(buf.limit());
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void methodIO(String p){
        try(FileInputStream in = new FileInputStream(new File(p))){
            byte[] b = new byte[200];
            int len;
            while( (len = in.read(b)) != -1){
                System.out.println(new String(b));
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
