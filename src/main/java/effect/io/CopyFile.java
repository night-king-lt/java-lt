package effect.io;

import java.io.*;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/6
 */
public class CopyFile {

    public static void main(String[] args) {
        String p1 = "src/main/resources/source.mp4";
        String p2 = "src/main/resources/output.mp4";
        run(p1, p2);
    }

    public static void run(String path1, String path2) {
        try(FileInputStream  in = new FileInputStream(new File(path1));
            FileOutputStream out = new FileOutputStream(new File(path2))
        ){
            byte[] b = new byte[200];
            int len;
            // int read(byte[] b): 读取多个字节,并保存到数组 b 中，从数组 b 的索引为 0 的位置开始存储，返回读取了几个字节
            while((len = in.read(b)) != -1){
                out.write(b, 0, len); // 把数组b 中的从 off 索引开始的 len 个字节写入到文件中
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}
