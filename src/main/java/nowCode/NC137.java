package nowCode;

import java.util.Stack;

/**
 * @Author liu.teng
 * @Date 2021/6/19
 *
 *   描述
 * 请写一个整数计算器，支持加减乘三种运算和括号。
 */
public class NC137 {

    public static void main(String[] args) {
        NC137 n = new NC137();
        String s = "1+2-(3*2-5)+(4-3*2)+1";
        String s2 = "(3+4)*(5+(2-3))";
        String s3 = "((10+2)*10-(100-(10+20*10-(2*3)))*10*1*2)-2";
        System.out.println(n.solve(s3));
    }

    public int solve (String s) {
        // write code here
        char[] c = s.toCharArray();
        Stack<Integer> intS = new Stack<>(); // 数字栈
        Stack<Character> syS = new Stack<>(); // 符号栈
        int result = 0;
        int i = 0;
        while(i < c.length){
            if (c[i] == '('){ // 左括号直接入栈
                syS.push(c[i]);
                i++;
            }else if (c[i] == ')'){
                while(syS.peek() != '('){  //  循环判断符号栈栈顶是否是左括号，若不是则计算数字栈顶的两个值
                    result = compute(intS.pop(), intS.pop(), syS.pop());
                    intS.push(result);  // 将计算好的结果压入数字栈
                }
                //此时遇到左括号，将左括号出栈即可
                syS.pop();
                i++;
            }else if (Character.isDigit(c[i])){  //判断是否是数字
                int j = i + 1;
                String num = "" + c[i];
                //计算多位数字字符构成的数字
                while(j < c.length && Character.isDigit(c[j])){
                    num = num + c[j];
                    j++;
                }
                //将数字入数字栈
                intS.push(strToInt(num));
                i = j;
            }else{
                //剩下的情况遇到运算符，比较当前运算符和栈顶运算符的优先级，若当前优先级大则直接入栈，
                if (syS.isEmpty() || compare(c[i], syS.peek())){
                    syS.push(c[i]);
                }else{ // 否则字符栈出栈，并计算数字栈头两个数字的值。将计算好的结果压入数字栈
                    result = compute(intS.pop(), intS.pop(), syS.pop());
                    intS.push(result);
                    syS.push(c[i]);
                }
                i++;
            }
        }
        // 最后循环计算 运算符栈里面的符号，直到为空
        while(!syS.isEmpty()){
            result = compute(intS.pop(), intS.pop(), syS.pop());
            intS.push(result);  // 将计算好的结果压入数字栈
        }
        return intS.pop();
    }

    /**
     *  计算方法
     */
    public int compute(int a, int b, char s){
        switch (s){
            case '+': return a + b;
            case '-': return b - a;
            case '*': return a * b;
        }
        System.out.println(s + "运算符不支持！目前只支持 + - * 三种运算符！");
        return 0;
    }

    public int strToInt(String s){
        return Integer.parseInt(s);
    }

    /**
     *  比较运算符优先级，如果是平级也返回true，即开始计算。
     *   排除掉一种特殊情况，当 前一个运算符是左括号 时，则直接入栈
     * @param after 后一个运算符
     * @param before  前一个运算符
     * @return
     */
    public boolean compare(char after, char before){
        if (before == '('){ // 排除掉一种特殊情况，当 前一个运算符是左括号 时，则直接入栈
            return true;
        }
        if (after == '*' && before != '*'){
            return true;
        }else if (after != '*' && before == '*'){
            return false;
        }
        return false;
    }
}
