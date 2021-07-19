package day.day.up.questions.algorithms.topics.data_structure.linked_list.number328;

import day.day.up.questions.algorithms.no_topics.number92.ListNode;

class Solution {
    public ListNode oddEvenList(ListNode head) {
        if (head==null || head.next==null) return head;

        ListNode oddEnd= head;
        ListNode evenEnd = head.next;
        while (evenEnd!=null&&evenEnd.next!=null){
            ListNode evenStart = oddEnd.next;
            oddEnd.next=evenEnd.next;
            oddEnd = oddEnd.next;

            ListNode temp = oddEnd.next;
            oddEnd.next=evenStart;

            evenEnd.next=temp;
            evenEnd=evenEnd.next;
        }

        return head;
    }
}