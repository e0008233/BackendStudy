package day.day.up.questions.algorithms.two_pointer.number142;


import day.day.up.questions.algorithms.number92.ListNode;

import java.util.HashSet;
import java.util.List;

public class Solution {
    public ListNode detectCycle(ListNode head) {
        HashSet<ListNode> set = new HashSet<>();
        ListNode temp = head;
        while (temp.next != null){
            if (set.contains(temp)){
                return temp;
            }else {
                set.add(temp);
            }
        }

        return null;
    }
}