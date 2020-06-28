package skill;

import java.io.*;

public class GetResource {
    public static void main(String[] args) throws Exception{
        String fileName = "/skill/BitMap.java";
        System.out.println(GetResource.class.getClassLoader().getResource("").getPath());
        System.out.println(GetResource.class.getClassLoader().getResource("/").getPath());
        InputStream is = GetResource.class.getClassLoader().getResourceAsStream(fileName);
        String cur_path = System.getProperty("user.dir");
        saveAsFile(is, cur_path, fileName);
    }

    public static void saveAsFile(InputStream is, String path, String fileName) throws IOException {
        File file = new File(path);
        if ( !file.exists() ){
            boolean f = file.mkdirs();
        }
        try(
            OutputStream os = new FileOutputStream(file.getPath() + File.separator + fileName)
        ){
            byte[] buf = new byte[1024];
            int len;
            while ( (len = is.read(buf)) != -1 ){
                os.write(buf, 0, len);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
