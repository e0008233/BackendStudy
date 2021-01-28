package day.day.up.questions.algorithms.number92;

/**
 * Definition for singly-linked list.
 */
public class ListNode {
    public int val;
    public ListNode next;

    public ListNode() {
    }

    public ListNode(int val) {
        this.val = val;
    }

    public ListNode(int val, ListNode next) {
        this.val = val;
        this.next = next;
    }

    public String toString() {
        String result = val + " ";
        if (next != null) {
            result += next.toString();
        }
        return result;
    }
}
