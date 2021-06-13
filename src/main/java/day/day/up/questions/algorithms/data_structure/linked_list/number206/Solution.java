package day.day.up.questions.algorithms.data_structure.linked_list.number206;

import day.day.up.questions.algorithms.number92.ListNode;

import java.util.List;


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