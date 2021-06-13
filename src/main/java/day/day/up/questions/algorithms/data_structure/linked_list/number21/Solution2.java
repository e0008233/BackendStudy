package day.day.up.questions.algorithms.data_structure.linked_list.number21;

import day.day.up.questions.algorithms.number92.ListNode;

public class Solution2 {
    public ListNode mergeTwoLists(ListNode l1, ListNode l2) {
        if (l1==null) return l2;
        if (l2==null) return l1;

        ListNode preHead = new ListNode();
        ListNode curr = preHead;
        if (l1!=null && l2!=null){
            if (l1.val<l2.val){
                curr.next=l1;
                l1=l1.next;
            }
            else{
                curr.next=l2;
                l2=l2.next;
            }

            curr = curr.next;
        }

        if (l1!=null){
            curr=l1;
        }
        if (l2!=null){
            curr=l2;
        }
        return preHead.next;
    }
}
