package effect.io;

import java.io.*;
import java.util.zip.GZIPOutputStream;

/**
 * @Author liuteng
 * @Date 2020/7/22 11:00 上午
 * @Version 1.0
 */
public class GzipStream {
    public static void main(String[] args){
        String text = "src/main/resources/wordCount.txt";
        String textGz = "src/main/resources/wordCount.gz";
        gzip2(text, textGz);
    }

    public static void gzip(String path1, String path2) {
        try(FileInputStream in = new FileInputStream(new File(path1));
            GZIPOutputStream gOut = new GZIPOutputStream(new FileOutputStream(new File(path2)))
        ){
            byte[] b = new byte[200];
            int len;
            while((len = in.read(b)) != -1){
                gOut.write(b, 0, len);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void gzip2(String path1, String path2) {
        FileInputStream in = null;
        GZIPOutputStream gOut = null;
        try{
            in = new FileInputStream(new File(path1));
            gOut = new GZIPOutputStream(new FileOutputStream(new File(path2)));
            byte[] b = new byte[200];
            int len;
            while((len = in.read(b)) != -1){
                gOut.write(b, 0, len);
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try{
                in.close();
            }catch (IOException e){
                e.printStackTrace();
            }
            try {
                gOut.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
