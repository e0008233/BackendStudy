package dp.number5;

public class Solution {
    public static String longestPalindrome(String s) {
        int longestLength = 0;
        String result = "";
        if (s.length()==1){
            return  s;
        }
        for (int i=0; i<s.length()-1;i++){
            String res1 = findaba(s,i);
            String res2 = findabba(s,i);
            if (res1.length()>longestLength){
                longestLength = res1.length();
                result= res1;
            }
            if (res2.length()>longestLength){
                longestLength = res2.length();
                result= res2;
            }

        }
        return result;
    }

    public static String findaba(String s, int index){
        int length = s.length();
        String result ="";
        int i=index;
        int j = index;
        while (i>=0 && j<=length-1){
            if (s.charAt(i)!=s.charAt(j)){
                break;
            }
            else{
                i=i-1;
                j=j+1;
            }
        }

        i=i+1;
        j=j-1;

        result = s.substring(i,j+1);

        return result;
    }

    public static String findabba(String s, int index){
        int length = s.length();
        String result ="";
        int i=index;
        int j = index+1;
        if (s.charAt(i)!=s.charAt(j)){
            return "";
        }
        while (i>=0 && j<=length-1){
            if (s.charAt(i)!=s.charAt(j)){
                break;
            }
            else{
                i=i-1;
                j=j+1;
            }
        }
        i=i+1;
        j=j-1;
        result = s.substring(i,j+1);
        return result;

    }
}
