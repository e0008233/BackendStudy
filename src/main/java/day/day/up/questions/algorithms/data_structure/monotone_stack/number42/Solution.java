package day.day.up.questions.algorithms.data_structure.monotone_stack.number42;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;

// 这个问题还有一个不错的解法就是使用单调栈Leetcode 单调栈问题总结（超详细！！！），
// 我们可以建立一个单调递减的栈，此时我们关注的问题就是如果出现一个元素比栈顶元素大的话，
// 那么此时必然可以形成凹槽，此时我们只需要计算凹槽长度和边界的高度差那么必然可以计算出接水的面积。
public class Solution {
    public int trap(int[] height) {
        if (height.length<3) return 0;
        int result = 0;
        Stack<Integer> stack = new Stack<>();

        for (int i=0;i<height.length;i++) {
            if (stack.isEmpty()) stack.add(i);
            else{
                int left = -1;
                int baseHeight = 0;
                while (!stack.isEmpty() && height[stack.peek()]<height[i]) {

                    left = stack.pop();
                    if (!stack.isEmpty()) {
                        int wall = Math.min(height[i],height[stack.peek()]);
                        int temp = (i - stack.peek()-1 ) * (wall - height[left]);
                        result = result +temp;
                    }
                }



                stack.add(i);
            }

        }

        return result;
    }
}