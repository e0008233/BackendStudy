import questions.algorithms.number92.*;

import java.util.ArrayList;
import java.util.List;

// Interesting question: number32(parentheses), number 84
public class main {
    public static void main(String[] args){
        Solution solution = new Solution();


        ListNode head = new ListNode(1,new ListNode(2));
//        ListNode a1 = new ListNode(2);
//        ListNode a2 = new ListNode(3);
//        ListNode a3 = new ListNode(4);
//        ListNode a4= new ListNode(5);
//        head.next=a1;
//        a1.next = a2;
//        a2.next = a3;
//        a3.next = a4;
//        a4.next = null;
        System.out.println(solution.reverseBetween(head,1,2));
    }
}