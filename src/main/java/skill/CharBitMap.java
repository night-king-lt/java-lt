package skill;

/**
 *  通过 位集合 存储大量数据，减少存储内存
 *  将 int（4字节）转化成 char（1字节） 内存缩减4倍
 */
public class CharBitMap {
    private final int length;
    private final char[] bitmap;
    private final char[] bitValue = new char[]{0b0000_0001, 0b0000_0010, 0b0000_0100, 0b0001_0000,
            0b0010_0000, 0b0100_0000, 0b1000_0000};

    public CharBitMap(int length){
        this.length = length;
        bitmap = new char[this.length];
    }

//    public static void intToChar(int n){
//        if (n < 0 || n > 1000){
//            throw new IllegalArgumentException("length value " + n + " is  illegal!");
//        }
//        // 求出该n所在bitMap的下标,等价于"n / 8"
//        int index =  n >> 8;
//        // 求出该值的偏移量(求余),等价于"n % 31"
//        int offset = (int) n & 31;
//        return index;
//    }
//
//    public static void main(String[] args) {
//        String c = intToChar(3);
//        System.out.println(c.getBytes());
//        System.out.println(Integer.parseInt(c, 2));
//        char a = 0b11;
//        System.out.println(a);
//
//    }
}
