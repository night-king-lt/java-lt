package nowCode;

/**
 * @Author liu.teng
 * @Date 2021/6/21 20:14
 * @Version 1.0
 */
public class NC128 {

    public static void main(String[] args) {
        NC128 s = new NC128();
        int[] arr = new int[]{2,1};
        System.out.println(s.maxWater(arr));
    }

    public long maxWater (int[] arr) {
        if (arr.length < 3) {
            return 0;
        }
        int low = 0;
        long sum = 0;
        long tmp = 0;
        //从左向右. 将所有以左边为短板的桶计算累加
        for (int i = 0; i < arr.length; i++) {
            if (arr[low] > arr[i]) {
                tmp = tmp + arr[low] - arr[i];
            }
            if (arr[low] <= arr[i]) {
                sum = sum + tmp;
                tmp = 0;
                low = i;
            }
        }
        low = arr.length-1;
        tmp = 0;
        //从右向左，再将所有以右边为短板的桶计算累加，左右等高的情况第一个循环已经计算上了，这里排除掉
        for (int j = arr.length-1; j >= 0; j--) {
            if (arr[low] > arr[j]) {
                tmp = tmp + arr[low] - arr[j];
            }
            //注意这里不能再 <=，否则可能会重复计算左右等高桶的情况
            if (arr[low] < arr[j]) {
                sum = sum + tmp;
                tmp = 0;
                low = j;
            }
        }
        return sum;
    }
}
