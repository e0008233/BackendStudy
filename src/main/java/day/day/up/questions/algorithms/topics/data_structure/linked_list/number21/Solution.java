package day.day.up.questions.algorithms.topics.data_structure.linked_list.number21;

import day.day.up.questions.algorithms.no_topics.number92.ListNode;

// recursion
class Solution {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1==null) return l2;
        if (l2==null) return l1;

        return helperRecursion(l1,l2);
    }

    private ListNode helperRecursion(ListNode l1, ListNode l2) {
        if (l1==null) return l2;
        if (l2==null) return l1;

        if (l1.val<l2.val){
            l1.next=helperRecursion(l1.next,l2);
            return l1;
        }
        else{
            l2.next=helperRecursion(l1,l2.next);
            return l2;
        }
    }
}