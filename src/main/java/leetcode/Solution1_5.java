package leetcode;

import java.util.*;

/**
 * @Author liu.teng
 * @Date 2020/9/23 17:26
 * @Version 1.0
 */
public class Solution1_5 {

    public static void main(String[] args) {
        //1.两数之和
//        int[] nums = new int[]{2, 7, 11, 15};
//        int target = 9;
//        System.out.println(Arrays.toString(twoSum(nums, target)));
        // 2. 两个链表相加
//        Solution.ListNode l1 = new Solution.ListNode(9);
//        l1.next = new Solution.ListNode(9);
//        Solution.ListNode l2 = new Solution.ListNode(1);
//        Solution.ListNode result = addTwoNumbers(l1, l2);
//        Solution.ListNode.print(result);
        // 3. 无重复的最长子串
//        String s = "pwwkew";
//        System.out.println(lengthOfLongestSubstring(s));
//        System.out.println(lengthOfLongestSubstring("abcabcbb"));
//        System.out.println(lengthOfLongestSubstring("bbbbb"));
//        System.out.println(lengthOfLongestSubstring("pwwkew"));
//        System.out.println(lengthOfLongestSubstring(" "));
//        System.out.println(lengthOfLongestSubstring("aua"));

        // 4. 寻找两个正序数组的中位数
//        int[] a = new int[]{1, 2, 3, 5, 7};
//        int[] b = new int[]{3, 4};
//        System.out.println(findMedianSortedArray(a, b));

        // 5. 最长回文子串
//        System.out.println(longestPalindrome("babad"));
//        System.out.println(longestPalindrome("cbbd"));
//
//        System.out.println(longestPalindromeDp("babad"));
//        System.out.println(longestPalindromeDp("cbbd"));
        System.out.println(longestPalindromeDp("baabccc"));

    }

    /**
     *    https://leetcode-cn.com/problemset/all/
     *    第一题 ： 两数之和
     * @param nums
     * @param target
     * @return
     */
    public static int[] twoSum(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i=0; i<nums.length; i++){
            int com = target - nums[i];
            if (map.containsKey(com) && map.get(com) != i){
                return new int[]{i, map.get(com)};
            }
            map.put(nums[i], i);
        }
        throw new IllegalArgumentException("No two sum solution");
    }

    /**
     *   第二题： 两个链表相加
     * @param l1
     * @param l2
     * @return
     */
    public static Solution1_5.ListNode addTwoNumbers(Solution1_5.ListNode l1, Solution1_5.ListNode l2) {
        Solution1_5.ListNode result = new Solution1_5.ListNode(0);
        Solution1_5.ListNode p = l1, q = l2, cur = result;
        // 个位数相加，初始进位数为0
        int carry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.val : 0;
            int y = (q != null) ? q.val : 0;
            int sum = x + y + carry;
            carry = sum / 10;
            cur.next = new Solution1_5.ListNode( sum % 10);
            cur = cur.next;
            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if(carry > 0){
            cur.next = new Solution1_5.ListNode(carry);
        }
        return result.next;
    }

    public static class ListNode {
        int val;
        ListNode next;
        ListNode() {}
        ListNode(int x) { val = x; }
        ListNode(int val, ListNode next) { this.val = val; this.next = next; }

        public static void print(Solution1_5.ListNode listNode){
            System.out.print(listNode.val);
            if (listNode.next == null){
                return;
            }
            System.out.print(" -> ");
            print(listNode.next);
        }
    }

    /**
     *   第三题：无重复字符的最长子串
     */
    public static int lengthOfLongestSubstring(String s) {
        int n = s.length();
        int res = 0;
        int end=0,start=0;
        Set<Character> set = new HashSet<>();
        while(start<n && end<n){
            if(set.contains(s.charAt(end))){
                set.remove(s.charAt(start++));
            }else{
                set.add(s.charAt(end++));
                res = Math.max(res,end-start);
            }

        }
        return res;
    }

    public static int lengthOfLongestSubstring2(String s) {
        int n = s.length();
        Map<Character, Integer> map = new HashMap<>();
        int result = 0;
        for (int j = 0, i = 0; j < n; j++){
            if (map.containsKey(s.charAt(j))) {
                i = Math.max(map.get(s.charAt(j)), i);
            }
            result = Math.max(result, j - i + 1);
            map.put(s.charAt(j), j + 1);
        }
        return result;
    }

    /**
     *  第四题：寻找两个正序数组的中位数
     */
    public static double findMedianSortedArray(int[] nums1, int[] nums2) {
        int n = nums1.length;
        int m = nums2.length;
        int left = (n + m + 1) / 2;
        int right = (n + m + 2) / 2;
        //将偶数和奇数的情况合并，如果是奇数，会求两次同样的 k 。
        return (getKth(nums1, 0, n - 1, nums2, 0, m - 1, left) + getKth(nums1, 0, n - 1, nums2, 0, m - 1, right)) * 0.5;

    }

    private static int getKth(int[] nums1, int start1, int end1, int[] nums2, int start2, int end2, int k) {
        int len1 = end1 - start1 + 1;
        int len2 = end2 - start2 + 1;
        //让 len1 的长度小于 len2，这样就能保证如果有数组空了，一定是 len1
        if (len1 > len2) return getKth(nums2, start2, end2, nums1, start1, end1, k);
        if (len1 == 0) return nums2[start2 + k - 1];

        if (k == 1) return Math.min(nums1[start1], nums2[start2]);

        int i = start1 + Math.min(len1, k / 2) - 1;
        int j = start2 + Math.min(len2, k / 2) - 1;

        if (nums1[i] > nums2[j]) {
            return getKth(nums1, start1, end1, nums2, j + 1, end2, k - (j - start2 + 1));
        }
        else {
            return getKth(nums1, i + 1, end1, nums2, start2, end2, k - (i - start1 + 1));
        }
    }

    /**
     *  第五题： 最长回文子串
     */
    // 动态规划1
    public static String longestPalindromeDp(String s){
        int n = s.length();
        if (n < 2){
            return s;
        }
        int si = 0, sj = 0;
        boolean[][] dp = new boolean[n][n];

        for (int k = 1; k <= n; k++){  //判断 k (1 ~ n)个字符是否回文
            for (int i = 0; i <= n - k; i++){
                if (s.charAt(i) == s.charAt(i + k - 1) && ( k < 3 || dp[i + 1][i + k - 2] )){ // k = 2  并且两个字符相等则一定是回文的
                    dp[i][i + k - 1] = true;
                    si = i;
                    sj = i + k - 1;
                }else {
                    dp[i][i + k - 1] = false;
                }
            }

        }
        return s.substring(si ,sj + 1);
    }

    // 马拉车算法
    public static String longestPalindrome(String s) {
        int len = s.length();
        if (len < 2){
            return s;
        }
        String str = addBoundaries(s, '#');
        int maxLen = 1;
        int start = 0;
        for (int i = 0; i < str.length(); i++){
            int step = centerSpread(str, i);
            if ( maxLen < step){
                maxLen = step;
                start = (i - maxLen)/2;
            }
        }
        return s.substring(start, start + maxLen);
    }

    public static int centerSpread(String s, int center){
        int len = s.length();
        int i = center - 1;
        int j = center + 1;
        int step = 0;
        while( i >= 0 && j < len && s.charAt(i) == s.charAt(j)){
            i--;
            j++;
            step++;
        }
        return step;
    }

    /**
     * 创建预处理字符串
     *
     * @param s      原始字符串
     * @param divide 分隔字符
     * @return 使用分隔字符处理以后得到的字符串
     */
    public static String addBoundaries(String s, char divide){
        int len = s.length();
        if (len == 0){
            return "";
        }
        if (s.indexOf(divide) != -1){
            throw new IllegalArgumentException("参数错误，您传递的分割字符，在输入字符串中存在！");
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (int i=0; i< len; i++){
            stringBuilder.append(divide).append(s.charAt(i));
        }
        stringBuilder.append(divide);
        return stringBuilder.toString();
    }

}
