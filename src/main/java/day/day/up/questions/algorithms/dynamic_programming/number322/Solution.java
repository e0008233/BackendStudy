package day.day.up.questions.algorithms.dynamic_programming.number322;


public class Solution {
    Integer[] dp;
    public int coinChange(int[] coins, int amount) {
        if (amount <1) return 0;
        dp = new Integer[amount+1];

        return helper(coins,amount);

    }

    public int helper(int[] coins, int amount) {
        if (dp[amount]!=null) return dp[amount];
        if (amount == 0) return 0;
        if (amount < 0) return -1;

        int result = Integer.MAX_VALUE;
        for(int i=0;i<coins.length;i++){
            if (coins[i]<=amount){
                int currentChoice = helper(coins,amount-coins[i]);
                if (currentChoice>=0 &&  currentChoice<result){
                    result = currentChoice+1;
                }
            }
        }
        if (result!=Integer.MAX_VALUE){
            dp[amount] = result;
        }else{
            dp[amount] = -1;
        }
        return dp[amount];
    }
}