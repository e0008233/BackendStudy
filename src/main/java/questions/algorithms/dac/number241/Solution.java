package questions.algorithms.dac.number241;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// When encountering a operator, calculate left and calculate right
public class Solution {
    HashMap<String, List<Integer>> ansMap = new HashMap<>();
    public List<Integer> diffWaysToCompute(String input) {

        if (ansMap.containsKey(input)) return ansMap.get(input);
        List<Integer> ans= new ArrayList<>();
        if ((!input.contains("+"))&&(!input.contains("-"))&&(!input.contains("*"))){
            ans.add(Integer.valueOf(input));
        }
        else{
            for (int i = 0; i<input.length(); i++){
                if (input.charAt(i)=='+' || input.charAt(i)=='-' ||input.charAt(i)=='*' ){
                    List<Integer> leftResults = diffWaysToCompute(input.substring(0,i));
                    List<Integer> rightResults = diffWaysToCompute(input.substring(i+1));

                    for (Integer left:leftResults){
                        for (Integer right:rightResults){
                            if (input.charAt(i)=='+'){
                                ans.add(Integer.valueOf(left)+Integer.valueOf(right));
                            }
                            else if (input.charAt(i)=='-'){
                                ans.add(Integer.valueOf(left)-Integer.valueOf(right));
                            }
                            else{
                                ans.add(Integer.valueOf(left)*Integer.valueOf(right));
                            }
                        }
                    }
                }
            }
        }
        ansMap.put(input,ans);
        return ans;
    }
}