package leetcode;

import java.util.*;

public class Solution2 {
    public static void main(String[] args) {
        int[] nums = new int[]{0,0,0};
        System.out.println(threeSum(nums));

    }

    public static List<List<Integer>> threeSum(int[] nums) {
        Arrays.sort(nums);
        List<List<Integer>> result = new ArrayList<>();
        for (int i=0; i< nums.length-2; i++){
            if (nums[i] <= 0 && (i==0 || nums[i] > nums[i-1])){
                int p = i + 1;
                int q = nums.length - 1;
                while ( q > p){
                    int sum = nums[i] + nums[p] + nums[q];
                    if (sum == 0){
                        result.add(Arrays.asList(nums[i], nums[p], nums[q]));
                        while(p < q && nums[p] == nums[p+1])p++; // 如果有重复数据，跳过
                        p++;
                        while(p < q && nums[q] == nums[q-1])q--; // 如果有重复数据，跳过
                        q--;
                    }else if (sum > 0){
                        q--;
                    }else {
                        p++;
                    }
                }
            }
        }
        return result;
    }
}
