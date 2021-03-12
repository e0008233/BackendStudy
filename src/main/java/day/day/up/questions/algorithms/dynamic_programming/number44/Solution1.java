package day.day.up.questions.algorithms.dynamic_programming.number44;


public class Solution1 {
    Boolean result[][];

    public boolean isMatch(String s, String p) {
//        System.out.println("___________________");
//        System.out.println("s: " + s + "length: " + s.length());
//        System.out.println("p: " + p);

        // +1 is for the s.subString(s.length()) which is an empty string
        result = new Boolean[s.length()+1][p.length()+1];
        Boolean dpResult =  dp(0,0,s,p);

        return dpResult;
    }

    public boolean dp(int i, int j, String text, String pattern){
        if (result[i][j]!=null){
            return result[i][j];
        }
        boolean isMatch= false;
        if (j==pattern.length()){
            // if pattern is empty
            isMatch = i==text.length();
        }
        else if(j==pattern.length()-1 && pattern.charAt(j)=='*'){
            isMatch = true;
        }
        else if (i==text.length()){
            //if text is empty
            isMatch = true;
            for (int index = j; index<pattern.length();index++){
                if (pattern.charAt(index)!='*') isMatch=false;
            }
        }
        else{
            if (pattern.charAt(j)=='*'){
                isMatch = dp(i,j+1,text,pattern) ||
                            dp(i+1,j,text,pattern) ||
                            dp(i+1,j+1,text,pattern);
            }else{
                isMatch = isFirstMatch(i,j,text,pattern) &&
                        dp(i+1,j+1,text,pattern);
            }
        }
        result[i][j] = isMatch;
        return isMatch;

    }
    public boolean isFirstMatch(int i, int j, String text, String pattern){
        if (i==text.length()) return false;
        if (text.charAt(i)==pattern.charAt(j) || pattern.charAt(j)=='?') return true;
        return false;
    }
}