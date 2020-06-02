package leetcode;

public class Solution6 {
    public String convert(String s, int numRows) {
        int n = s.length();
        if (numRows < 2){
            return s;
        }
        StringBuilder result = new StringBuilder();
        char[] arr = s.toCharArray();

        int step = 2 * numRows - 2;

        // 第一行
        for (int i = 0; i < n; i += step){
            result.append(arr[i]);
        }

        // 中间行
        for (int row = 1; row < numRows - 1; row++){
            int current = row, currentNeibor;
            while(current < n){
                result.append(arr[current]);
                currentNeibor = current + step - 2 * row;
                if (currentNeibor > 0 && currentNeibor < arr.length){
                    result.append(arr[currentNeibor]);
                }
                current += step;
            }
        }

        // 最后一行
        for (int i = numRows - 1; i < n; i += step){
            result.append(arr[i]);
        }

        return result.toString();

    }

    public static void main(String[] args) {
        Solution6 solution6 = new Solution6();
        System.out.println(solution6.convert("PAYPALISHIRING", 3));
        System.out.println(solution6.convert("PAYPALISHIRING", 4));
        System.out.println(solution6.convert("A", 1));
        System.out.println(solution6.convert("AB", 1));
    }
}
