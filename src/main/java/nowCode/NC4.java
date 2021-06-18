package nowCode;

/**
 * @Author liu.teng
 * @Date 2021/6/18
 *
 *   描述
 * 判断给定的链表中是否有环。如果有环则返回true，否则返回false。
 * 你能给出空间复杂度 O(1) 的解法么？
 * 输入分为2部分，第一部分为链表，第二部分代表是否有环，然后回组成head头结点传入到函数里面。-1代表无环，其他的数字代表有环，这些参数解释仅仅是为了方便读者自测调试
 */
public class NC4 {

    /**
     *  定义快慢双指针，
     *   论证：假设有环，不管是O型还是6型环，指针都会走到环上。
     *        设 快指针比慢指针多走了 n 个节点，那么再循环 （环-n）次，快节点就能追上慢节点，
     *        因为每次循环，快节点就追了慢节点一个节点。
     * @param head
     * @return
     */
    public boolean hasCycle(ListNode<Integer> head) {
        ListNode<Integer> fast = head; // 快指针
        ListNode<Integer> low = head;  // 慢指针
        while( fast != null && fast.next != null){
            fast = fast.next.next;  // 快指针每次走两个节点
            low = low.next;   // 慢指针每次走一个节点
            if( fast == low ){ // 如果两个指针相遇，则证明有环
                return true;
            }
        }
        return false;
    }
}
