package day.day.up.questions.algorithms.topics.dynamic_programming.number32;

// wrong one
public class Solution {
    public int longestValidParentheses(String s) {
        if (s.isEmpty()) return 0;
        int max = 0;
        int numberOfPairs = 0;
        int count = 0;  //stack
        for (int i = 0; i<s.length(); i++){
            if (s.charAt(i)=='('){
                count ++;
            }
            else if (s.charAt(i)==')'){
                count --;
                if (count<0){
                    count = 0;
                    if (numberOfPairs>max){
                        max = numberOfPairs;
                    }
                    numberOfPairs = 0;
                }
//                else if (count==0){
//                    if (i!=0)
//                    numberOfPairs++;
//                }
                else{
                    numberOfPairs++;
                }
            }
        }
        if (numberOfPairs>max) max = numberOfPairs;
        return max*2;
    }
}