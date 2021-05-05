package day.day.up.questions.algorithms.dynamic_programming.number279;


import java.util.Arrays;

// This is a dp problem. The key is to find the relation which is dp[i] = min(dp[i], dp[i-square]+1).
// For example, dp[5]=dp[4]+1=1+1=2.
// Iterate through all the possible squares
public class Solution2 {
    public int numSquares(int n) {
        int max = (int) Math.sqrt(n);

        int[] dp = new int[n+1];
        Arrays.fill(dp, Integer.MAX_VALUE);

        for(int i=1; i<=n; i++){
            for(int j=1; j<=max; j++){
                if(i==j*j){
                    dp[i]=1;
                }else if(i>j*j){
                    dp[i]=Math.min(dp[i], dp[i-j*j] + 1);
                }
            }
        }

        return dp[n];
    }
}
