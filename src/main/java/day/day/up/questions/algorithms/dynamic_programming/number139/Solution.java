package day.day.up.questions.algorithms.dynamic_programming.number139;

import java.util.List;

class Solution {
    public boolean wordBreak(String s, List<String> wordDict) {
        boolean[] result = new boolean[s.length()+1];
        result[0] = true;

        // similar to perfect square
        for (int i=1;i<=s.length();i++){
            for (String word:wordDict){
                if (i>=word.length()){
                    if (s.substring(i-word.length(),i).equals(word)){
                        result[i] = result[i] || result[i-word.length()];
                    }
                }
            }
        }

        return result[s.length()];
    }
}