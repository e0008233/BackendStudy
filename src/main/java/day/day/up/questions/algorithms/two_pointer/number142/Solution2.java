package day.day.up.questions.algorithms.two_pointer.number142;


import day.day.up.questions.algorithms.number92.ListNode;

import java.util.HashSet;

public class Solution2 {
    public ListNode detectCycle(ListNode head) {
        if (head==null||head.next==null) return null;

        ListNode slowPointer = head;
        ListNode fastPointer = head;

        boolean isMet = false;
        while(fastPointer!=null && fastPointer.next!=null){
            slowPointer=slowPointer.next;
            if (isMet) {
                fastPointer = fastPointer.next;
            }
            else{
                fastPointer = fastPointer.next.next;
            }
            if (slowPointer==fastPointer){
                if (isMet){
                    return slowPointer;
                }
                else{

                    //only exception: cycle begins at head
                    if (fastPointer==head) return head;
                    isMet = true;
                    fastPointer=head;
                }
            }
        }

        return null;
    }
}