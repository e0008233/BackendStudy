package day.day.up.questions.algorithms.number17;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
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
        helper("",digits);
        return ans;

    }

    public void helper (String prefix, String digits){
        if (digits.isEmpty()){
            ans.add(prefix);
            return;
        };


        String letters = phone.get(digits.charAt(0));
        for (int j = 0; j<letters.length();j++){
            helper(prefix+Character.toString(letters.charAt(j)), digits.substring(1));
        }

    }
}