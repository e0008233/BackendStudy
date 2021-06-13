package day.day.up.questions.algorithms.data_structure.linked_list.number143;

import day.day.up.questions.algorithms.number92.ListNode;

//        1) Find the middle point using tortoise and hare method.
//        2) Split the linked list into two halves using found middle point in step 1.
//        3) Reverse the second half.
//        4) Do alternate merge of first and second halves.
public class Solution {

    // fist method in place: find the last
    public void reorderList(ListNode head) {
    }

    public void reorderList2(ListNode head) {
        if (head==null || head.next==null) return;

        ListNode slow = head, fast = slow.next;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
    }


    private ListNode reverseListHelper(ListNode head) {
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