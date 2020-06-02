package leetcode;

/**
 *
 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。

 示例 1：

 输入: "babad"
 输出: "bab"
 注意: "aba" 也是一个有效答案。
 示例 2：

 输入: "cbbd"
 输出: "bb"
 */
public class Solution5 {
    public static void main(String[] args) {
        Solution5 solution5 = new Solution5();
        System.out.println(solution5.longestPalindrome("babad"));
        System.out.println(solution5.longestPalindrome("cbbd"));

        System.out.println(solution5.longestPalindromeDp("babad"));
        System.out.println(solution5.longestPalindromeDp("cbbd"));

    }

    // 动态规划1
    public String longestPalindromeDp(String s){
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
    public String longestPalindrome(String s) {
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

    public int centerSpread(String s, int center){
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
    public String addBoundaries(String s, char divide){
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
