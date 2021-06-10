package day.day.up.questions.algorithms.data_structure.monotone_stack.number503;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

class Solution {
    public int[] nextGreaterElements(int[] nums) {
        int[] result = new int[nums.length];
        Arrays.fill(result,-1);
        Stack<Integer> stack = new Stack<>();

        for (int i=0;i<nums.length*2;i++){
            while(!stack.isEmpty() && nums[stack.peek()]<nums[i%nums.length]){
                int temp = stack.pop();
                result[temp] = nums[i%nums.length];
            }
            stack.add(i%nums.length);
        }

        return result;
    }
}