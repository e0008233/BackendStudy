package day.day.up.questions.algorithms.data_structure.linked_list.number24;

import day.day.up.questions.algorithms.number92.ListNode;

// recursion
class Solution {
    public ListNode swapPairs(ListNode head) {
        if (head==null || head.next==null) return head;

        ListNode curr = head;
        ListNode prev = new ListNode();
        ListNode result = prev;

        prev.next=head;
        while(curr!=null && curr.next!=null){
            ListNode temp = curr.next;
            curr.next = temp.next;
            temp.next=curr;
            prev.next=temp;

            prev = curr;
            curr=curr.next;
        }

        return result.next;
    }
}