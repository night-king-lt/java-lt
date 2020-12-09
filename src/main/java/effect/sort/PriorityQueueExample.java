package effect.sort;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * @Author liu.teng
 * @Date 2020/12/8 19:19
 * @Version 1.0
 */
public class PriorityQueueExample {
    public static void main(String[] args) {
        //优先队列自然排序示例
        Queue<Integer> integerPriorityQueue = new PriorityQueue<>(7, (a, b) -> (b - a));

        Random rand = new Random();

        for(int i = 0; i < 7; i++){
            integerPriorityQueue.add(rand.nextInt(100));

        }
        for(int i = 0; i < 7; i++){
            Integer in = integerPriorityQueue.poll();
            System.out.println("Processing Integer:" + in);

        }
    }
}
