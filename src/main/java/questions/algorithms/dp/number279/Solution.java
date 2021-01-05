package questions.algorithms.dp.number279;

// iterate through all the possible squares for n, and keep the minimum

public class Solution {
    static Integer[] dp;
    public int numSquares(int n) {
        if (n==0) return 0;
        if (n==1) return 1;
        dp = new Integer[n+1];

        int num = helper(n);
        return num;
    }

    public int helper(int n){
        if (dp[n]!=null) return dp[n];
        if (n==0){
            dp[n] = 0;
            return 0;
        }
        else if (n==1){
            dp[n] = 1;
            return 1;
        }
        else{
            int result = Integer.MAX_VALUE;
            for (int i=1; i<=Math.sqrt(n);i++){
                result = Math.min(result,helper(n-i*i)+1);
            }
            dp[n] = result;
        }
        return dp[n];
    }
}