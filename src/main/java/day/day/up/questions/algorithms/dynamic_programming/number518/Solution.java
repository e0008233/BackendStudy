package day.day.up.questions.algorithms.dynamic_programming.number518;


// dp[i][j]的定义如下：
// 若只使用前i个物品，当背包容量为j时，有dp[i][j]种方法可以装满背包。
class Solution {
    public int change(int amount, int[] coins) {

        if (amount == 0)
            return 1;
        else if (coins.length == 0)
            return 0;


        int[][] dp = new int[coins.length + 1][amount + 1];
        for (int i = 1; i <= coins.length; i++) {
            dp[i][0] = 1;
        }

        //如果你不把这第i个物品装入背包，也就是说你不使用coins[i]这个面值的硬币，那么凑出面额j的方法数dp[i][j]应该等于dp[i-1][j]，继承之前的结果。
        //如果你把这第i个物品装入了背包，也就是说你使用coins[i]这个面值的硬币，那么dp[i][j]应该等于dp[i][j-coins[i-1]]。

        for (int i = 1; i <= coins.length; i++) {
            for (int j = 1; j <= amount; j++) {
                dp[i][j] = dp[i - 1][j];//不加入这枚硬币
                if (j - coins[i - 1] >= 0)//加入这枚硬币
                    dp[i][j] += dp[i][j - coins[i - 1]];
            }
        }
        return dp[coins.length][amount];

    }

}