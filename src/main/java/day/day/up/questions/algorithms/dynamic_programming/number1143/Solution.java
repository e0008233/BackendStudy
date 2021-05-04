package day.day.up.questions.algorithms.dynamic_programming.number1143;

import java.util.Arrays;

class Solution {
    public int longestCommonSubsequence(String text1, String text2) {
        int[][] result = new int[text1.length()][text2.length()];
        for (int i=0;i<text1.length();i++){
            for (int j=0;j<text2.length();j++){
                if (text1.charAt(i)==text2.charAt(j)){
                    if (i==0||j==0){
                        result[i][j] = 1;
                    }
                    else{
                        result[i][j]=result[i-1][j-1]+1;
                    }
                }
                else{
                    int row = i-1<0?0:i-1;
                    int col = j-1<0?0:j-1;
                    result[i][j] = Math.max(result[row][j],result[i][col]);
                }
            }
        }
        return result[text1.length()-1][text2.length()-1];
    }
}