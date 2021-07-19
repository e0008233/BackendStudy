package day.day.up.questions.algorithms.topics.data_structure.monotone_stack.number496;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class Solution {
    public int[] nextGreaterElement(int[] nums1, int[] nums2) {
        Map<Integer,Integer> map = new HashMap<>();
        Stack<Integer> stack = new Stack<>();
        for (int num:nums1){
            map.put(num,-1);
        }
        for (int num:nums2){
            while(!stack.isEmpty() && stack.peek()<num){
                int temp = stack.pop();
                map.put(temp,num);
            }
            stack.add(num);
        }
        for (int i=0;i<nums1.length;i++){
            nums1[i]=map.get(nums1[i]);
        }
        return nums1;
    }
}