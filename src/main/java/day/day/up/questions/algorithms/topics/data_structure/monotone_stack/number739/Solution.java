package day.day.up.questions.algorithms.topics.data_structure.monotone_stack.number739;

import java.util.Arrays;
import java.util.Stack;

class Solution {
    public int[] dailyTemperatures(int[] temperatures) {
        int[] result = new int[temperatures.length];
        Arrays.fill(result,0);
        Stack<Integer> stack = new Stack<>();

        for (int i=0;i<temperatures.length;i++){
            while(!stack.isEmpty() && temperatures[stack.peek()]<temperatures[i]){
                int temp = stack.pop();
                result[temp] = i-temp;
            }
            stack.add(i);
        }

        return result;
    }
}