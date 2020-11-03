package leetcode;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Solution {

    public static void main(String[] args) {
        int[][] op = new int[][]{{1,1,1},{1,2,2},{1,3,2},{2,1},{1,4,4},{2,2}};
        int k = 3;
        int[] result = LRU(op, k);
        for (int next: result){
            System.out.println(next);
        }
    }

    /**
     * lru design
     * @param operators int整型二维数组 the ops
     * @param k int整型 the k
     * @return int整型一维数组
     */
    public static int[] LRU(int[][] operators, int k) {
        // write code here
        int len = (int) Arrays.stream(operators).filter(x -> x[0] == 2).count();
        int[] res = new int[len];
        LRUMap lmap = new LRUMap(k);
        for(int i = 0, j = 0; i < operators.length; i++){
            if(operators[i][0] == 1){
                lmap.set(operators[i][1], operators[i][2]);
            }else{
                res[j++] = lmap.get(operators[i][1]);
            }
        }

        return res;
    }


    static class LRUMap{
        HashMap<Integer, Integer> map;
        int capacity;
        Queue<Integer> useCommon;

        LRUMap(int capacity){
            this.capacity = capacity;
            map = new HashMap<>();
            useCommon = new ConcurrentLinkedQueue<>();
        }

        public void set(int key, int val){
            useCommon.add(key);
            if(map.size() < capacity){
                map.put(key, val);
            }else{
                map.remove(useCommon.poll());
            }
        }

        public int get(int key){
            useCommon.add(key);
            if(map.containsKey(key)){
                return map.get(key);
            }
            return -1;
        }
    }
}