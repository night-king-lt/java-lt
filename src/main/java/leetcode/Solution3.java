package leetcode;

import jdk.nashorn.internal.ir.WhileNode;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Solution3 {
    public static void main(String[] args) {
        ListNode l1 = new ListNode(9);
        l1.next = new ListNode(9);
//        ListNode.print(l1);

        ListNode l2 = new ListNode(1);
//        ListNode.print(l2);
        ListNode result = addTwoNumbers(l1, l2);
        ListNode.print(result);

    }

    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode result = new ListNode(0);
        ListNode p = l1, q = l2, cur = result;
        // 个位数相加，初始进位数为0
        int carry = 0;
        while (p != null || q != null) {
            int x = (p != null) ? p.val : 0;
            int y = (q != null) ? q.val : 0;
            int sum = x + y + carry;
            carry = sum / 10;
            cur.next = new ListNode( sum % 10);
            cur = cur.next;
            if (p != null) p = p.next;
            if (q != null) q = q.next;
        }
        if(carry > 0){
            cur.next = new ListNode(carry);
        }
        return result.next;
    }

    public static class ListNode {
       int val;
       ListNode next;
       ListNode(int x) { val = x; }

       public static void print(ListNode listNode){
           System.out.print(listNode.val);
           if (listNode.next == null){
               return;
           }
           System.out.print(" -> ");
           print(listNode.next);
       }
    }
}
