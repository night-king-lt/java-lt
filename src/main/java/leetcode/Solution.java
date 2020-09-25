package leetcode;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author liu.teng
 * @Date 2020/9/23 17:26
 * @Version 1.0
 */
public class Solution {
    /**
     *   无重复字符的最长子串
     */
    public static int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int res = 0;
        int end=0,start=0;
        Set<Character> set=new HashSet<>();
        while(start<n && end<n){
            if(set.contains(s.charAt(end))){
                set.remove(s.charAt(start++));
            }else{
                set.add(s.charAt(end++));
                res=Math.max(res,end-start);
            }

        }
        return res;
    }

    public static void main(String[] args) {
        String s = "pwwkew";
        System.out.println(lengthOfLongestSubstring(s));
    }

}
