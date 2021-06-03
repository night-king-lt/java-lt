package leetcode;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Solution {

    public static void main(String[] args) {
        int[] input = new int[]{4,5,1,6,2,7,3,8};
        int k = 4;
        ArrayList<Integer> result = GetLeastNumbers_Solution(input, k);
//        result.forEach(System.out::println);
    }

    /**
     * 给定一个数组，找出其中最小的K个数。例如数组元素是4,5,1,6,2,7,3,8这8个数字，
     * 则最小的4个数字是1,2,3,4。如果K>数组的长度，那么返回一个空的数组
     *
     * 输入：[4,5,1,6,2,7,3,8],4
     * 输出：[1,2,3,4]
     */
    public static ArrayList<Integer> GetLeastNumbers_Solution(int [] input, int k) {
        ArrayList<Integer> result = new ArrayList<>();
        if(k > input.length){
            return result;
        }
        // 定义一个大顶堆，即从大到小输出的优先队列
        Queue<Integer> queue = new PriorityQueue<>(k, (a, b) -> (b - a));
        for (int i = 0; i < input.length; i++){
            if(queue.size() < k){ // 如果队列没有k个元素，填满
                queue.add(input[i]);
            }else{ // 如果队列达到k个元素，则判断当前元素是否大于堆顶元素
                if (queue.element() > input[i]){
                    queue.poll();
                    queue.add(input[i]);
                }
            }
        }
        while(queue.size() > 0){
            System.out.println(queue.poll());
        }
        result.addAll(queue);
        return result;
    }
}