package day.day.up.questions.algorithms.dp.number10;


//Approach 1: Recursion
//        Intuition
//
//        If there were no Kleene stars (the * wildcard character for regular expressions), the problem would be easier - we simply check from left to right if each character of the text matches the pattern.
//
//        When a star is present, we may need to check many different suffixes of the text and see if they match the rest of the pattern. A recursive solution is a straightforward way to represent this relationship.

public class Solution {
    public boolean isMatch(String s, String p) {
        if (p.isEmpty()) return s.isEmpty();

        if (p.length()>=2 && p.charAt(1)=='*'){
            if (isFirstMatch(s,p)){
                return isMatch(s, p.substring(2)) || isMatch(s.substring(1),p);
            }
            else {
                return isMatch(s, p.substring(2));
            }

        }
        else{
            return isFirstMatch(s,p) && isMatch(s.substring(1),p.substring(1));
        }
    }

    public boolean isFirstMatch(String s, String p){
        // s: "", p:"a*" case is handled above
        if ( s.isEmpty()) return false;
        if (s.charAt(0)==p.charAt(0)) return true;
        if (p.charAt(0)=='.') return true;
        return false;
    }
}