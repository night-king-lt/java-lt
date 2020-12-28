package test;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * @Author liu.teng
 * @Date 2020/10/23 10:13
 * @Version 1.0
 */
public class three {
        public static void main(String[] args) {
            System.out.println(JumpFloor(4));
        }

    public static int JumpFloor(int target) {
        if(target < 3){
            return target;
        }
        int result = 0;
        int one = 1;
        int two = 2;
        for(int i = 3; i <= target; i++){
            result = one + two;
            one = two;
            two = result;
        }
        return result;
    }

}
