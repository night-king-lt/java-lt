package effect.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileAdd {
    public static void main(String[] args) {
        String p1 = "src/main/resources/source.mp4";
        String p2 = "src/main/resources/output2.mp4";
        run(p1, p2);
    }

    public static void run(String p1, String p2){
        try(FileInputStream in = new FileInputStream(new File(p1));
            FileOutputStream out = new FileOutputStream(new File(p2), false)){
            byte[] b = new byte[20000];
            int len;
            while( (len = in.read(b)) >= 2000){
                printBytes(b);
//                out.write(b, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void printBytes(byte[] b){
        for (byte next: b){
            System.out.print(next + " ");
        }
    }
}
