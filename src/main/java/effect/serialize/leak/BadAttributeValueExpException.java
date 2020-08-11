package effect.serialize.leak;

import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * @Author liu.teng
 * @Date 2020/8/11 19:39
 * @Version 1.0
 */
public class BadAttributeValueExpException  extends Exception   {

    /* Serial version */
    private static final long serialVersionUID = -3105272988410493376L;

    /**
     * @serial A string representation of the attribute that originated this exception.
     * for example, the string value can be the return of {@code attribute.toString()}
     */
    private Object val;

    /**
     * Constructs a BadAttributeValueExpException using the specified Object to
     * create the toString() value.
     *
     * @param val the inappropriate value.
     */
    public BadAttributeValueExpException (Object val) {
        this.val = val == null ? null : val.toString();
    }

    /**
     * Returns the string representing the object.
     */
    public String toString()  {
        return "BadAttributeValueException: " + val;
    }

    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ObjectInputStream.GetField gf = ois.readFields();
        Object valObj = gf.get("val", null);

        if (valObj == null) {
            val = null;
        } else if (valObj instanceof String) {
            val= valObj;
        } else if (System.getSecurityManager() == null
                || valObj instanceof Long
                || valObj instanceof Integer
                || valObj instanceof Float
                || valObj instanceof Double
                || valObj instanceof Byte
                || valObj instanceof Short
                || valObj instanceof Boolean) {
            val = valObj.toString();
        } else { // the serialized object is from a version without JDK-8019292 fix
            val = System.identityHashCode(valObj) + "@" + valObj.getClass().getName();
        }
    }
}