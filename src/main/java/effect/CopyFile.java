package effect;

import org.apache.hadoop.mapred.FileInputFormat;

import java.io.*;

/**
 * @author liuteng
 * @version 1.0
 * @date 2020/7/6
 */
public class CopyFile {

    public static void main(String[] args) throws IOException {
        CopyFile c = new CopyFile();
        String p1 = "src/main/resources/source.mp4";
        String p2 = "src/main/resources/output.mp4";
        c.run(p1, p2);
    }

    public void run(String path1, String path2) throws IOException {
        FileInputStream  in = new FileInputStream(new File(path1));
        FileOutputStream out = new FileOutputStream(new File(path2));
        byte[] b = new byte[200];
        int len;
        while((len = in.read(b)) != -1){
            out.write(b, 0, len);
        }
    }

}
