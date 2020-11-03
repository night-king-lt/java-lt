package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author liu.teng
 * @Date 2020/10/23 10:13
 * @Version 1.0
 */
public class three {
        public static void main(String[] args) {
            Scanner in = new Scanner(System.in);
            while (in.hasNextInt()) {// 注意，如果输入是多个测试用例，请通过while循环处理多个测试用例
                int a = in.nextInt();
                sushu(a);
            }
        }

    public static boolean is_prime(int j, List<Integer> list) {
        for(int i= 0; i< list.size(); i++){
            if( list.get(i) < Math.sqrt(j) + 1){
                if(j % list.get(i) == 0)  return false;
            }else{
                return true;
            }
        }
        return true;
    }

    public static void sushu(int n){
            List<Integer> tmp = new ArrayList<>();
            int j = 2;
            while(tmp.size() < n){
                if(is_prime(j, tmp)){
                    tmp.add(j);
                }
                j++;
            }
            System.out.println(byteToHex(tmp.get(n - 1)));

    }

    private final static String[] hexArray = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f"};

    public static String byteToHex(int n) {
        if (n < 0) {
            n = n + 256;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return hexArray[d1] + hexArray[d2];
    }
}
