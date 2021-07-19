package day.day.up.questions.algorithms.topics.data_structure.linked_list.number19;

import day.day.up.questions.algorithms.no_topics.number92.ListNode;


class Solution {
    public ListNode removeNthFromEnd(ListNode head, int n) {
        // if we have one node and need to delete that
        ListNode dummy = new ListNode();
        dummy.next = head;
        ListNode first = dummy;
        ListNode second = dummy;
        // Advances first pointer so that the gap between first and second is n nodes apart
        for (int i = 1; i <= n + 1; i++) {
            first = first.next;
        }
        // Move first to the end, maintaining the gap
        while (first != null) {
            first = first.next;
            second = second.next;
        }
        second.next = second.next.next;
        return dummy.next;
    }
}