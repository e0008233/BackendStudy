package day.day.up.questions.algorithms.dynamic_programming.number121;


import java.util.List;

public class Solution {
    public int maxProfit(int[] prices) {
        int[] dp = new int[prices.length];

        if (prices.length==1) return 0;
        int maxProfit = 0;
        dp[0] = prices[0]; //current smallest value ending at i
        for (int i=1;i<prices.length;i++){
            if (prices[i]>dp[i-1]) dp[i] = dp[i-1];
            else dp[i] = prices[i];
        }

        for (int i=1;i<prices.length;i++){
            if (prices[i]-dp[i]>maxProfit) maxProfit = prices[i]-dp[i];
        }

        return maxProfit;
    }
}