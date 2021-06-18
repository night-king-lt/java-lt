package nowCode;

/**
 * @Author liu.teng
 * @Date 2021/6/18
 *
 *  描述
 * 请实现有重复数字的升序数组的二分查找
 * 给定一个 元素有序的（升序）整型数组 nums 和一个目标值 target  ，写一个函数搜索 nums 中的第一个出现的target，如果目标值存在返回下标，否则返回 -1
 */
public class NC105 {

    public static int search (int[] nums, int target) {
        // write code here
        int left = 0;
        int right = nums.length - 1;
        while( left <= right){
            int mid = (right + left)/2;
            if(nums[mid] < target){
                left = mid + 1;
            }else if(nums[mid] > target){
                right = mid - 1;
            }else{
                // 如果相等，需要递归往左判断是否都相等
                while(mid > 0 && nums[mid - 1] == nums[mid]){
                    mid = mid - 1;
                }
                return mid;
            }
        }
        return -1;
    }

    public static void main(String[] args) {
        /**  示例1
         * [1,2,4,4,5],4
         * 2
         */
        System.out.println(search(new int[]{1, 2, 4, 4, 5}, 4));

        /** 示例2
         * [1,2,4,4,5],3
         * -1
         */
        System.out.println(search(new int[]{1, 2, 4, 4, 5}, 3));

        /** 示例3
         * [1,1,1,1,1],1
         * 0
         */
        System.out.println(search(new int[]{1, 1, 1, 1, 1}, 1));
    }
}
