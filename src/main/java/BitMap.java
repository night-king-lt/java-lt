
public class BitMap {
    private long length;
    private static int[] bitmap;
    private static final int[] BIT_VALUE = {0x00000001, 0x00000002, 0x00000004, 0x00000008, 0x00000010, 0x00000020,
            0x00000040, 0x00000080, 0x00000100, 0x00000200, 0x00000400, 0x00000800, 0x00001000, 0x00002000, 0x00004000,
            0x00008000, 0x00010000, 0x00020000, 0x00040000, 0x00080000, 0x00100000, 0x00200000, 0x00400000, 0x00800000,
            0x01000000, 0x02000000, 0x04000000, 0x08000000, 0x10000000, 0x20000000, 0x40000000, 0x80000000};

    public BitMap(long length){
        this.length = length;
        bitmap = new int[ (int) length >> 5 + ((length & 31) > 0 ? 1 :0)];
    }

    public void setN(long n){
        if (n < 0 || n > length){
            throw new IllegalArgumentException("length value "+n+" is  illegal!");
        }
        int index = (int) n >> 5;
        int offset = (int) n & 31;

        bitmap[index] |= BIT_VALUE[offset];
    }

    /**
     * 获取值N是否存在
     * @return 1：存在，0：不存在
     */
    public int isExist(long n){
        if (n < 0 || n > length){
            throw new IllegalArgumentException("length value "+n+" is  illegal!");
        }
        int index = (int) n >> 5;
        int offset = (int) n & 31;
        int bits = bitmap[index];
        return (bits & BIT_VALUE[offset]) >>> offset;
    }

    public static void main(String[] args) {
        BitMap b = new BitMap(4_0000_0000);
        b.setN(8);
        int[] result = b.bitmap;
        System.out.println("bitmapSize: " + result.length);
        for (int i=0; i < result.length; i ++){
            if (!Integer.toBinaryString(result[i]).equals("0")){
                System.out.println("i: " + i);
                System.out.println(Integer.toBinaryString(result[i]));
            }
        }
    }
}
