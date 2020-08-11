package effect.serialize;

import java.io.*;
import java.lang.reflect.InvocationTargetException;

/**
 * @Author liu.teng
 * @Date 2020/8/11 17:53
 * @Version 1.0
 */
public class ReadObject implements Serializable {
    private void readObject(java.io.ObjectInputStream stream)
            throws IOException, ClassNotFoundException{
        System.out.println("read object in ReadObject");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        byte[] serializeData=serialize(new ReadObject());
        deserialize(serializeData);

        Object runtime=Class.forName("java.lang.Runtime")
                .getMethod("getRuntime",new Class[]{})
                .invoke(null);

        Class.forName("java.lang.Runtime")
                .getMethod("exec", String.class)
                .invoke(runtime,"calc.exe");
    }
    public static byte[] serialize(final Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        return out.toByteArray();
    }
    public static Object deserialize(final byte[] serialized) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
    }

}
