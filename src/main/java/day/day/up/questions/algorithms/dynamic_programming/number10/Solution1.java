package day.day.up.questions.algorithms.dynamic_programming.number10;

// dp: use result[][] to record all the intermediate result
public class Solution1 {

    static Boolean result[][];
    static String text;
    static String pattern;

    public boolean isMatch(String s, String p) {

        result = new Boolean[s.length()+1][p.length()+1];
        text = s;
        pattern = p;
        System.out.println(result);

        boolean isMatch =  dp(0,0);
//        System.out.println(result);

        return isMatch;
    }

    public boolean dp(int i, int j){
        boolean ans;
        if (result[i][j]!=null) return result[i][j];
        if (j==pattern.length()){
            ans = i==text.length();
        }

        else {

            if (pattern.length() > j + 1 && pattern.charAt(j + 1) == '*') {
                if (isFirstMatch(i, j)) {
                    ans = dp(i + 1, j) || dp(i, j + 2);
                } else {
                    ans = dp(i, j + 2);
                }
            } else {
                ans = isFirstMatch(i, j) && dp(i + 1, j + 1);
            }
        }
        result[i][j]= ans;
        return ans;
    }

    public boolean isFirstMatch(int i, int j){
        if (i>=text.length()) return false;
        return text.charAt(i)==pattern.charAt(j)||pattern.charAt(j)=='.';
    }

}
