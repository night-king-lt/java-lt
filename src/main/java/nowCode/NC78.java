package nowCode;

/**
 * @Author liu.teng
 * @Date 2021/6/18
 *
 *    输入一个链表，反转链表后，输出新链表的表头。
 */
public class NC78 {
    public ListNode<Integer> ReverseList(ListNode<Integer> head) {
        // 判断链表为空或长度为1的情况
        if (head == null || head.next == null){
            return head;
        }
        ListNode<Integer> pre = null; //当前节点的前一个节点
        ListNode<Integer> next; // 当前节点的下一个节点
        while (head != null){
            next = head.next; // 记录当前节点的下一个节点位置
            head.next = pre; // 让当前节点指向前一个节点位置，完成反转
            pre = head;  // pre 往右走
            head = next; // 当前节点往右继续走
        }
        return pre;
    }

}
