package day.day.up.questions.algorithms.topics.math.number382;

import day.day.up.questions.algorithms.no_topics.number92.ListNode;

/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
class Solution {

    ListNode head;
    /** @param head The linked list's head.
    Note that the head is guaranteed to be not null, so it contains at least one node. */
    public Solution(ListNode head) {
        this.head = head;
    }

    /** Returns a random node's value. */
    public int getRandom() {
        ListNode curr = this.head;
        int ans= curr.val;
        int size = 1;
        while(curr!=null){
            if(Math.random() < 1.0 / size){
                ans=curr.val;
            }
            size++;
            curr=curr.next;
        }

        return ans;

    }
}
/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(head);
 * int param_1 = obj.getRandom();
 */