package day.day.up.questions.algorithms.topics.dynamic_programming.number474;


import javafx.util.Pair;

public class Solution {
    public int findMaxForm(String[] strs, int m, int n) {
        // 0-1 backpack problem, take or not take strs
        // dp[i][j][k] only using [0,i] strs, j of 0s and k of 1s
        int[][][] dp = new int[strs.length+1][m+1][n+1];

        for (int i=1;i<=strs.length;i++){
            for (int j=0;j<=m;j++){
                for (int k=0;k<=n;k++){
                    Pair<Integer,Integer> counts = countZeroAndOne(strs[i-1]);
                    int numOfZero = counts.getKey();
                    int numOfOne = counts.getValue();
                    if (j>=numOfZero&&k>=numOfOne){
                        dp[i][j][k] = Math.max(dp[i-1][j-numOfZero][k-numOfOne] + 1,dp[i-1][j][k]);
                    }
                    else{
                        dp[i][j][k] = dp[i-1][j][k];

                    }
                }
            }
        }
        return dp[strs.length][m][n];
    }

    public Pair<Integer, Integer> countZeroAndOne(String str){
        int m=0;
        int n=0;
        for (int i=0;i<str.length();i++){
            if (str.charAt(i)=='0') m++;
            else if (str.charAt(i)=='1') n++;
        }

        return new Pair<>(m,n);
    }
}