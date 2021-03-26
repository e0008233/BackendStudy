package day.day.up.questions.algorithms.search.number17;


import java.util.*;

// https://www.geeksforgeeks.org/iterative-letter-combinations-of-a-phone-number/
// Using queue for bfs
// using stack for dfs

// adding every possible values to the popped value, until the length is met
public class Solution1 {

    List<String> ans;
    Map<Character, String> phone = new HashMap<Character, String>() {{
        put('2', "abc");
        put('3', "def");
        put('4', "ghi");
        put('5', "jkl");
        put('6', "mno");
        put('7', "pqrs");
        put('8', "tuv");
        put('9', "wxyz");
    }};

    public List<String> letterCombinations(String digits) {
        ans = new ArrayList<>();
        if (digits.isEmpty()) return ans;
        Stack<String> stack = new Stack<>();
        stack.add("");
        while (!stack.isEmpty()){
            String toRemove = stack.pop();
            if (toRemove.length()==digits.length()){
                ans.add(toRemove);
            }
            else{
                String value = phone.get(digits.charAt(toRemove.length()));
                for (int i=0; i<value.length(); i++){
                    stack.add(toRemove+value.charAt(i));
                }
            }
        }
        return ans;

    }
}
