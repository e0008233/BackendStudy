package day.day.up.questions.algorithms.dynamic_programming.number309;

public class Solution {
    public Integer dp[][];
    public int maxProfit(int[] prices) {

        // if you can do as many transactions as possible
        int result = 0;

        dp= new Integer[prices.length][3];

        return helper(prices,0,1);

    }

    private int helper(int[] prices, int index, int state) {

        if (index>=prices.length) return 0;
        if (dp[index][state] !=null ) return dp[index][state];

        int result = 0;

        // 0:can buy, 1: can sell, 2: cool down
        if (state==0){
            // only can buy current stock, or do nothing
            // case 1 (buy current stock): as transaction is not completed, do not increase. profit = - price[i] + potential sell
            result = Math.max(-prices[index]+helper(prices,index+1,1),
                    helper(prices,index+1,0));
        }
        else if (state==1){
            // only can sell current stock, or do nothing
            result = Math.max(prices[index]+helper(prices,index+1,2),
                    helper(prices,index+1,1));
        }
        else{
            // cool down: do nothing, can sell next day
            result = helper(prices,index+1,0);
        }
        dp[index][state] = result;

        return result;
    }
}