package day.day.up.questions.algorithms.topics.two_pointer.number142;


import day.day.up.questions.algorithms.no_topics.number92.ListNode;

import java.util.HashSet;

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