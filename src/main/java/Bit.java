import java.util.Properties;

public class Bit {
    public static void main(String[] args) {
        int a = 536870911;
        System.out.println(Integer.toBinaryString(a));
        int b = -5 >>> 3;
        System.out.println(Integer.toBinaryString(b));

        Properties p = new Properties();
        p.put("a","1");
        Properties p1 = new Properties(p), p2 = p;
        p1.put("b", "2");
        p2.put("b", "3");

        System.out.println(p);
        System.out.println(p1);
        System.out.println(p2);
    }
}
