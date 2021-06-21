package nowCode;

/**
 * @Author liu.teng
 * @Date 2021/6/19
 *
 *   描述
 * 写出一个程序，接受一个字符串，然后输出该字符串反转后的字符串。（字符串长度不超过1000）
 */
public class NC103 {
    public String solve(String str) {
        // write code here
        StringBuffer sb =new StringBuffer(str);//此方法针对的是io流，不能针对字符串。
        return sb.reverse().toString();
    }

    /**
     *  原地交换
     *  时间复杂度 O(n),额外空间复杂度 O(1)
     */
    public static String solve2(String str){
        char[] cstr = str.toCharArray();
        int len = cstr.length;
        for (int i = 0; i < len / 2; i++){
            char tmp = cstr[i];
            cstr[i] = cstr[len - 1 - i];
            cstr[len - 1 - i] = tmp;
        }
        return new String(cstr);
    }

    public static void main(String[] args) {
        System.out.println(solve2("abcdef"));
    }
}
