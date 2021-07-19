package day.day.up.questions.algorithms.topics.data_structure.linked_list.number143;

import day.day.up.questions.algorithms.no_topics.number92.ListNode;


//        1) Find the middle point using tortoise and hare method.
//        2) Split the linked list into two halves using found middle point in step 1.
//        3) Reverse the second half.
//        4) Do alternate merge of first and second halves.
public class Solution {



    public void reorderList(ListNode head) {



        if (head==null || head.next==null) return;

        ListNode slow = head, fast = head, curr2= head;
        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }

        if (fast!=null){
            ListNode temp = slow.next;
            slow.next=null;
            slow=temp;
        }

        ListNode reverseHead = reverse(slow);

        while(reverseHead!=null){
            ListNode temp = curr2.next;
            curr2.next = reverseHead;

            ListNode temp2 = reverseHead.next;
            if (temp!=reverseHead){
                reverseHead.next = temp;
            }

            reverseHead=temp2;
            curr2=temp;
        }
    }


    private ListNode reverse(ListNode slow) {
        ListNode prev = null;
        ListNode curr = slow;
        while (curr.next!=null){
            ListNode temp = curr.next;
            curr.next=prev;

            prev=curr;
            curr=temp;
        }

        curr.next=prev;

        return curr;


    }
}