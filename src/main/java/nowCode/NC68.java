package nowCode;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author liu.teng
 * @Date 2021/6/19
 *   描述
 * 一只青蛙一次可以跳上1级台阶，也可以跳上2级。求该青蛙跳上一个n级的台阶总共有多少种跳法（先后次序不同算不同的结果）。
 */
public class NC68 {

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     *   jumpFloor(n) = jumpFloor(n-1) + jumpFloor(n-2)
     *
     * 优点，代码简单好写，缺点：慢，会超时
     * 时间复杂度：O(2^n)
     * 空间复杂度：递归栈的空间
     */
    public long jumpFloor(int target) {
        if (target < 3){
            return target;
        }
        return jumpFloor(target - 1) + jumpFloor(target - 2);
    }

    /**
     *  动态规划，从下而上只运行一次得出答案
     *  时间复杂度：O（n）
     *  空间复杂度：O（1）  只保存了 one，two，result这三个变量，不随target增长而增长
     */
    public long jumpFloor2(int target) {
        if(target < 3){
            return target;
        }
        long result = 0; // 当前台阶跳法 =  one + two
        long one = 1;   // 存前两步台阶有多少种跳法
        long two = 2;   // 存前一步台阶有多少种跳法
        for(int i = 3; i <= target; i++){
            result = one + two;
            one = two;
            two = result;
        }
        return result;
    }

    public static void main(String[] args) {
        NC68 m = new NC68();
        int target = 50;
        System.out.println("start: " + formatter.format(new Date(System.currentTimeMillis())));
        System.out.println(m.jumpFloor(target));
        System.out.println("end: " + formatter.format(new Date(System.currentTimeMillis())));

        System.out.println("start: " + formatter.format(new Date(System.currentTimeMillis())));
        System.out.println(m.jumpFloor2(target));
        System.out.println("end: " + formatter.format(new Date(System.currentTimeMillis())));
    }
}
