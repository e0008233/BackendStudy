package day.day.up.questions.algorithms.topics.divide_and_conquer.number395;

import java.util.*;
//     - Divide the problem into subproblems. (Divide Phase).
//     - Repeatedly solve each subproblem independently and combine the result to solve the original
//       problem. (Conquer Phase).
//
// We could apply this strategy by recursively splitting the string into substrings and
// combine the result to find the longest substring that satisfies the given condition.
// The longest substring for a string starting at index start and ending at index end can be given by,
//
// longestSustring(start, end) = max(longestSubstring(start, mid), longestSubstring(mid+1, end))
//
//
// Finding the split position (mid)
//
// The string would be split only when we find an invalid character. An invalid character is the one
// with a frequency of less than k. As we know, the invalid character cannot be part of the result,
// we split the string at the index where we find the invalid character, recursively check for each split,
// and combine the result.

public class Solution {

    public int longestSubstring(String s, int k) {
        HashMap<Character,Integer> countMap = new HashMap<>();
        HashSet<Character> splitKeys = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            if (countMap.containsKey(s.charAt(i))) {
                countMap.put(s.charAt(i), countMap.get(s.charAt(i)) + 1);
            } else {
                countMap.put(s.charAt(i), 1);
            }
        }
        for (Map.Entry<Character,Integer> entry : countMap.entrySet()) {
            if (entry.getValue() < k) splitKeys.add(entry.getKey());
        }
        int ans = s.length();
        for (Character character:splitKeys){
            for (int i=0; i<s.length();i++){
                if (s.charAt(i)==character){
                    return Math.max(longestSubstring(s.substring(0,i),k),longestSubstring(s.substring(i+1),k));
                }
            }
        }
        return ans;

    }

}