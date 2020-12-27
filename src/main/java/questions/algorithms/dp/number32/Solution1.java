package questions.algorithms.dp.number32;

import java.util.Stack;

public class Solution1 {

    public int longestValidParentheses(String s) {
        int maxAns = 0;
        Stack<Integer> stack = new Stack<>();
        stack.push(-1); //marking the starting position
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.push(i);
            } else {
                stack.pop();
                if (stack.empty()) {
                    stack.push(i);
                } else {
                    // i - stack.peek() is important as it only calculates the index of used '('
                    // fix "()(()" counted as 4 issue
                    maxAns = Math.max(maxAns, i - stack.peek());
                }
            }
        }
        return maxAns;
    }
}