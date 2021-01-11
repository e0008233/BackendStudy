package questions.algorithms.number92;

import java.util.List;

public  class Solution {
    public ListNode reverseBetween(ListNode head, int m, int n) {
        if (head==null) return null;
        if (m==n) return head;
        int i=1;
        ListNode prevNode = null;
        ListNode currentNode = head;

        ListNode connectionHeadPrev = new ListNode();
        ListNode connectionHead = new ListNode();
        do{

            if (i==m){
                connectionHeadPrev = prevNode;
                connectionHead = currentNode;
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            else if (i>m && i<n){
                ListNode nextNode = currentNode.next;
                currentNode.next = prevNode;
                prevNode = currentNode;
                currentNode = nextNode;
            }
            else if (i==n){
                if (m!=1) {
                    connectionHeadPrev.next = currentNode;
                }
                connectionHead.next=currentNode.next;
                currentNode.next = prevNode;

                if (m==1) head=currentNode;

                break;
            }
            else{
                prevNode = currentNode;
                currentNode = currentNode.next;
            }
            i++;
        } while(currentNode!=null);

        return head;
    }
}