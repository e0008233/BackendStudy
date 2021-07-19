package day.day.up.questions.algorithms.topics.two_pointer.number76;

import java.util.HashMap;

// like finding the minimum, ending at each character
public class Solution {
    public String minWindow(String s, String t) {
        if (t.isEmpty()) return "";

        int indexL = 0;
        int indexR = 0;

        HashMap<Character,Integer> patternMap = new HashMap<>();
        HashMap<Character,Integer> windowMap = new HashMap<>();
        int matchCount=0;
        String result="";
        int minLength = Integer.MAX_VALUE;

        for (int i = 0; i<t.length();i++){
            if (patternMap.containsKey(t.charAt(i))){
                patternMap.put(t.charAt(i),patternMap.get(t.charAt(i))+1);
            }
            else{
                patternMap.put(t.charAt(i),1);
            }
        }

        while (indexR<=s.length()-1){
            Character current = s.charAt(indexR);

            matchCount=addCharacter(patternMap,windowMap,current,matchCount);


            while (matchCount==t.length()){
                if(indexR-indexL+1<minLength){
                    result = s.substring(indexL,indexR+1);
                    minLength = indexR-indexL+1;
                }


                //moving left index to find minimum

                matchCount=removeCharacter(patternMap,windowMap,s.charAt(indexL),matchCount);
                indexL++;
            }

            indexR++;



        }

        return result;
    }

    private int removeCharacter(HashMap<Character, Integer> patternMap, HashMap<Character, Integer> windowMap, Character current, int count) {
        if (patternMap.containsKey(current)){
            if (windowMap.get(current)>0){
                windowMap.put(current,windowMap.get(current)-1);
                if (windowMap.get(current)<patternMap.get(current)){
                    count--;
                }
            }
        }

        return count;
    }

    private int addCharacter(HashMap<Character,Integer> patternMap,HashMap<Character,Integer> windowMap,Character current, int count){
        if (patternMap.containsKey(current)){
            if (!windowMap.containsKey(current)) {
                windowMap.put(current,1);
                count++;
            }else if (windowMap.get(current)<patternMap.get(current)){
                windowMap.put(current, windowMap.get(current) + 1);
                count++;
            }
            else{
                windowMap.put(current, windowMap.get(current) + 1);
            }
        }

        return count;
    }
}