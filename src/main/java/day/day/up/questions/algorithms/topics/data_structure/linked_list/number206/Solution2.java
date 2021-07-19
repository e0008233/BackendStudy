package day.day.up.questions.algorithms.topics.data_structure.linked_list.number206;

import day.day.up.questions.algorithms.no_topics.number92.ListNode;

public class Solution2 {
    public ListNode reverseList(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode p = reverseList(head.next);
        // head.next is already reversed
        head.next.next = head;
        head.next = null;
        return p;
    }
}
