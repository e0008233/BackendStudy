package day.day.up.questions.algorithms.topics.two_pointer.number344;


public class Solution {
    public void reverseString(char[] s) {
        if (s.length==0||s.length==1) return;

        int left = 0;
        int right = s.length-1;
        while (left<right){
            swap(s,left,right);
            left++;
            right--;
        }
    }

    private void swap(char[] s, int left, int right) {
        char temp = s[left];
        s[left] = s[right];
        s[right] = temp;
    }
}