package day.day.up.questions.algorithms.topics.data_structure.linked_list.number206;

import day.day.up.questions.algorithms.no_topics.number92.ListNode;


class Solution {
    public ListNode reverseList(ListNode head) {
        if (head == null) return null;
        ListNode curr = head;
        ListNode prev = null;
        while(curr.next!=null){
            ListNode temp = curr.next;
            curr.next=prev;
            prev=curr;
            curr=temp;
        }
        curr.next=prev;
        return curr;
    }
}