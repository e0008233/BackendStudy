package day.day.up.questions.algorithms.topics.dynamic_programming.number188;


// https://hashnode.com/post/best-time-to-buy-and-sell-stocks-a-comprehensive-guide-cko8xg43m0oz194s114kz71pn
// https://www.youtube.com/watch?v=Z_6KmNGK_eY
public class Solution {
    public Integer dp[][][];
    public int maxProfit(int k, int[] prices) {

        // if you can do as many transactions as possible
        int result = 0;
        if(k> prices.length/2){
            for (int i=1;i<prices.length;i++){
                if (prices[i]-prices[i-1]>0){
                    result += prices[i]-prices[i-1];
                }
            }
            return result;
        }
        dp= new Integer[k+1][prices.length][2];

        return helper(prices,0,0,1,k);

    }

    private int helper(int[] prices, int index, int numOfTx, int isAllowedToBuy,int txLimit) {

        if (numOfTx >= txLimit || index>=prices.length) return 0;
        if (dp[numOfTx][index][isAllowedToBuy] !=null ) return dp[numOfTx][index][isAllowedToBuy];

        int result = 0;
        if (isAllowedToBuy==1){
            // only can buy current stock, or do nothing
            // case 1 (buy current stock): as transaction is not completed, do not increase. profit = - price[i] + potential sell
            result = Math.max(-prices[index]+helper(prices,index+1,numOfTx,0,txLimit),
                    helper(prices,index+1,numOfTx,1,txLimit));
        }
        else{
            // only can sell current stock, or do nothing
            result = Math.max(prices[index]+helper(prices,index+1,numOfTx+1,1,txLimit),
                    helper(prices,index+1,numOfTx,0,txLimit));
        }
        dp[numOfTx][index][isAllowedToBuy] = result;

        return result;
    }
}