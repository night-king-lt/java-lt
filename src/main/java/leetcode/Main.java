package leetcode;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * @Author liu.teng
 * @Date 2020/11/16 16:03
 * @Version 1.0
 */
public class Main {
    public static void main(String[] args){
//        ListNode head = new ListNode(1);
//        ListNode result = deleteDuplicates(head);
//        System.out.println(result);
//        String a = "abcd";
//        System.out.println(a.substring(1,3));

        System.out.println(solve("11110"));
    }

    public static int solve(String nums) {
        // write code here
        if(nums.length() == 0 || nums.charAt(0) == '0'){
            return 0;
        }
        int[] dp = new int[nums.length()];
        dp[0] = 1;
        for(int i = 1; i < dp.length; i++){
            if(nums.charAt(i) != '0'){ // 如果该为非0
                dp[i] = dp[i - 1];
            }
            // 获取当前数据和前一个数字的组合
            int num = Integer.parseInt(nums.substring(i - 1, i + 1));
            if(num >= 10 && num <= 26){
                if(i == 1){
                    dp[i] += 1;
                }else{
                    dp[i] += dp[i - 2]; // 如果i位是0，那么dp[i]为初始值0，num=10或者20，dp[i] = dp[i-2];
                }
            }
        }
        return dp[nums.length() - 1];
    }

    public static ListNode deleteDuplicates(ListNode head) {
        // write code here
        Map<Integer, Integer> map = new HashMap<>();
        // 第一次遍历，记录各元素出现次数
        ListNode p = head;
        while(p != null){
            if(map.containsKey(head.val)){
                map.put(head.val, map.get(head.val) + 1);
            }else{
                map.put(head.val, 1);
            }
            p = p.next;
        }
        // 第二次遍历，删除重复节点
        ListNode dummy = new ListNode(-1);
        dummy.next = head;
        ListNode pre = dummy;
        ListNode cur = head;
        while(cur != null){
            if(map.get(cur.val) > 1){
                pre.next = cur.next;
            }else{
                pre = pre.next;
            }
            cur = cur.next;
        }
        return dummy.next;
    }


    static class ListNode {
       int val;
        ListNode(int val){
            this.val = val;
        }
       ListNode next = null;

        @Override
        public String toString() {
            final StringBuffer sb = new StringBuffer("ListNode{");
            sb.append("val=").append(val);
            sb.append(", next=").append(next);
            sb.append('}');
            return sb.toString();
        }
    }

}
