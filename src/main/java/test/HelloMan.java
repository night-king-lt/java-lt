package test;

/**
 * @Author liu.teng
 * @Date 2020/10/22 11:18
 * @Version 1.0
 */
public class HelloMan {
    public static void main(String[] args) {
        test(true);
    }

    public static void test(boolean f){
        if (f){
            return;
        }
        System.out.println("ss");
    }

    public static String change(int in){
        int result = 0;
        char[] inArray = String.valueOf(in).toCharArray();
        for (int i= inArray.length -1 ; i >= 0; i-- ){
            int n = Integer.parseInt(String.valueOf(inArray[i]));
            int index = inArray.length - 1 - i;
            int tmp =  n * (int) Math.pow(3, index);
            result += tmp;
        }

        return String.valueOf(result);
    }


}
