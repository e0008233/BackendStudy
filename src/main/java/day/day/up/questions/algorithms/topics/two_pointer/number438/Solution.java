package day.day.up.questions.algorithms.topics.two_pointer.number438;


import io.swagger.models.auth.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solution {
    public List<Integer> findAnagrams(String s, String p) {
        HashMap<Character,Integer> map = new HashMap<>();
        HashMap<Character,Integer> curr = new HashMap<>();

        List<Integer> ans = new ArrayList<>();
        for (int i=0;i<p.length();i++){
            if (map.containsKey(p.charAt(i))){
                map.put(p.charAt(i),map.get(p.charAt(i))+1);
            }
            else{
                map.put(p.charAt(i),1);
            }
        }

        int start = 0;

        int validCharacters = 0;

        for (int end=0;end<s.length();end++){
            char c = s.charAt(end);
            if (map.containsKey(c)){
                if (curr.containsKey(c)){
                    curr.put(c,curr.get(c)+1);
                }
                else{
                    curr.put(c,1);
                }

                if (map.get(c).equals(curr.get(c))) validCharacters++;

                if (validCharacters == map.size()) ans.add(start);
            }

            while (end-start>=p.length()-1){
                char k = s.charAt(start);
                start++;
                if (map.containsKey(k)){
                    if (map.get(k).equals(curr.get(k))) validCharacters--;
                    curr.put(k,curr.get(k)-1);

                }
            }
        }


        return ans;
    }
}