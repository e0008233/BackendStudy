package day.day.up.questions.algorithms.data_structure.linked_list.number160;

import day.day.up.questions.algorithms.number92.ListNode;

import java.util.List;


//假设链表 A 的头节点到相交点的距离是 a，链表 B 的头节点到相交点的距离是 b，相交点 到链表终点的距离为 c。我们使用两个指针，分别指向两个链表的头节点，
//并以相同的速度前进， 若到达链表结尾，则移动到另一条链表的头节点继续前进。按照这种前进方法，两个指针会在 a + b + c 次前进后同时到达相交节点。
//如果不相交，则在a+c+b+c 一起到达终点null
public class Solution {
    public ListNode getIntersectionNode(ListNode headA, ListNode headB) {
        ListNode currA = headA;
        ListNode currB = headB;

        while (currA!=currB){
            currA=currA!=null?currA.next:headB;
            currB=currB!=null?currB.next:headA;

        }
        return null;
    }
}