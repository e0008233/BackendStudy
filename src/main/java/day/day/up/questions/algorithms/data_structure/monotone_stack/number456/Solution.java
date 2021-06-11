package day.day.up.questions.algorithms.data_structure.monotone_stack.number456;

import org.omg.PortableInterceptor.INACTIVE;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class Solution {
    public boolean find132pattern(int[] nums) {

        int right = Integer.MIN_VALUE;
        Stack<Integer> stack = new Stack<>();

        for (int i=nums.length-1;i>=0;i--){
            if (nums[i]<right) return true;
            while (!stack.isEmpty()&&stack.peek()<nums[i]){
                right=stack.pop();
            }
            stack.add(nums[i]);
        }

        return false;
    }
}