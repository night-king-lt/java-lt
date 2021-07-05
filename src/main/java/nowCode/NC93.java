package nowCode;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author liu.teng
 * @Date 2021/6/18 16:26
 *   描述
 * 设计LRU缓存结构，该结构在构造时确定大小，假设大小为K，并有如下两个功能
 * set(key, value)：将记录(key, value)插入该结构
 * get(key)：返回key对应的value值
 * [要求]
 * set和get方法的时间复杂度为O(1)
 * 某个key的set或get操作一旦发生，认为这个key的记录成了最常使用的。
 * 当缓存的大小超过K时，移除最不经常使用的记录，即set或get最久远的。
 * 若opt=1，接下来两个整数x, y，表示set(x, y)
 * 若opt=2，接下来一个整数x，表示get(x)，若x未出现过或已被移除，则返回-1
 * 对于每个操作2，输出一个答案
 *
 *  示例
 *   输入：
 * [[1,1,1],[1,2,2],[1,3,2],[2,1],[1,4,4],[2,2]],3
 * 返回值：
 * [1,-1]
 * 说明：
 * 第一次操作后：最常使用的记录为("1", 1)
 * 第二次操作后：最常使用的记录为("2", 2)，("1", 1)变为最不常用的
 * 第三次操作后：最常使用的记录为("3", 2)，("1", 1)还是最不常用的
 * 第四次操作后：最常用的记录为("1", 1)，("2", 2)变为最不常用的
 * 第五次操作后：大小超过了3，所以移除此时最不常使用的记录("2", 2)，加入记录("4", 4)，并且为最常使用的记录，然后("3", 2)变为最不常使用的记录
 */
public class NC93 {

    private final Map<Integer, Node> map = new HashMap<>(); // 记录双向链表里的值，为了快速查询到key
    private Node head = new Node(-1, -1);
    private Node tail = new Node(-1, -1);
    private int k;

    public int[] LRU (int[][] operators, int k) {
        // write code here
        this.k = k;
        // 构建一个双向链表
        head.next = tail;
        tail.prev = head;
        int len = (int) Arrays.stream(operators).filter(x -> x[0] == 2).count();  // 计算出get操作的次数
        int[] res = new int[len];
        for(int i = 0, j = 0; i < operators.length; i++){
            if(operators[i][0] == 1){
                set(operators[i][1], operators[i][2]);
            }else{
                res[j++] = get(operators[i][1]);
            }
        }

        return res;
    }

    /**
     *  set  时间复杂度 O(1)
     * @param key
     * @param val
     */
    public void set(int key, int val){
        // key已存在，则更新value
        if(get(key) > -1){
            map.get(key).val = val;
        }else{
            if(map.size() == k){  // map已满
                int rk = tail.prev.key; // 获取最靠尾节点的节点rk的key
                map.remove(rk);  // map去掉该k，v
                tail.prev.prev.next = tail; // 尾结点的前两个节点指向尾节点，即跨过要删除的节点rk
                tail.prev = tail.prev.prev; // 尾结点指向尾结点的前两个节点，即跨过要删除的节点rk
            }
            Node node = new Node(key, val);
            moveToHead(node);  // 将目标节点插入到头节点的后面，记为最常用的节点
            map.put(key, node); // 记录链表里已经存的数据，方便快速查询key
        }
    }

    /**
     *  get  时间复杂度 O(1)
     * @param key
     * @return
     */
    public int get(int key){
        if(map.containsKey(key)){  // 如果map包含key
            Node node = map.get(key);  // 获取到该节点
            node.prev.next = node.next;  // 链表中删除该节点
            node.next.prev = node.prev;
            moveToHead(node); // 将该节点移到头节点的后面，记为最常用的节点
            return node.val;
        }
        return -1;
    }

    /**
     *  将node节点移到头结点后面
     * @param node
     */
    public void moveToHead(Node node){
        node.next = head.next;
        head.next.prev = node;
        head.next = node;
        node.prev = head;
    }

    /**
     *   双向链表数据结构
     */
    private static class Node{
        int key, val;
        Node prev, next;
        public Node(int key, int val){
            this.key = key;
            this.val = val;
        }
    }
}
