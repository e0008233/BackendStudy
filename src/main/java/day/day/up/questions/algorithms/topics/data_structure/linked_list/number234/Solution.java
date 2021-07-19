package day.day.up.questions.algorithms.topics.data_structure.linked_list.number234;

import day.day.up.questions.algorithms.no_topics.number92.ListNode;

// 先使用快慢指针找到链表中点，再把链表切成两半;然后把后半段翻转;最后比较两半是否 相等。
class Solution {
    public boolean isPalindrome(ListNode head) {
        if (head==null || head.next==null) return true;
        ListNode slow = head;
        ListNode fast = head;


        while(fast!=null && fast.next!=null){
            slow=slow.next;
            fast=fast.next.next;
        }

        if (fast!=null){
            slow = slow.next;
        }

        ListNode reverseHead = reverse(slow);
        while (reverseHead!=null){
            if (head.val!=reverseHead.val) return false;
            head= head.next;
            reverseHead=reverseHead.next;
        }
        return true;
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