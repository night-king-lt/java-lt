package nowCode;

import java.util.Arrays;

/**
 * @Author liu.teng
 * @Date 2021/7/2 11:01
 * @Version 1.0
 */
public class NC55 {
    /**
     *  对字符串数组进行排序，然后只要比较首尾两个字符串即可
     * @param strs
     * @return
     */
    public String longestCommonPrefix (String[] strs) {
        // write code here
        if (strs == null || strs.length == 0){
            return "";
        }
        Arrays.sort(strs);
        int minSize = Math.min(strs[0].length(), strs[strs.length - 1].length());
        int i = 0;
        for ( ; i < minSize; i++){
            if (strs[0].charAt(i) != strs[strs.length - 1].charAt(i)){
                break;
            }
        }
        return strs[0].substring(0, i);
    }

    public static void main(String[] args) {

    }
}
