package questions.algorithms.dp.number5;


// Time complexity: O(n^2)
// Iterating through the String, taking each character with/without the following one as the center and expanding in 2 directions
// tracking the longest one
public class Solution2 {
    public static String longestPalindrome(String s) {
        if (s.length()==1){
            return  s;
        }
        String result="";

        for (int i=0; i<s.length();i++){
            String temp = expand(s,i,i);
            if (temp.length()>result.length()) result= temp;
            if (i<=s.length()-2 && s.charAt(i)==s.charAt(i+1)){

                String temp2 = expand(s,i,i+1);
                if (temp2.length()>result.length()) result= temp2;
            }
        }
        return result;
    }

    private static String expand(String s, int start, int end) {

        String result = s.substring(start,end+1);
        while (start>=0 && end<=s.length()-1){
            if (s.charAt(start)==s.charAt(end)){
                result=s.substring(start,end+1);
                start--;
                end++;

            }
            else return result;
        }
        return result;
    }
}
