package day.day.up.questions.algorithms.dynamic_programming.number416;

class Solution {
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num:nums){
            sum+=num;
        }
        if (nums.length==1 || sum%2 ==1) return false;

        sum = sum /2;
        // dp[i][j]: only using i and i before elements to get sum of j
        boolean [][] dp = new boolean[nums.length+1][sum+1];

        for (int i=0;i<=nums.length;i++){
            dp[i][0] = true;
        }


        for (int i=1;i<=nums.length;i++){
            for (int j=nums[i-1];j<=sum;j++){
                // choose i or not choose i
                if (j-nums[i-1]>=0) {
                    dp[i][j] = dp[i - 1][j] || dp[i-1][j - nums[i-1]];
                }
                else {
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[nums.length][sum];
    }
}