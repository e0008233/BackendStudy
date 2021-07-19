package day.day.up.questions.algorithms.topics.dynamic_programming.number416;

public class Solution {
    public boolean canPartition(int[] nums) {
        int sum = 0;
        for (int num:nums){
            sum+=num;
        }
        if (nums.length==1 || sum%2 ==1) return false;

        int n=nums.length;
        int target = sum/2;
        // dp[i][j]: only using i and i before elements to get sum of j
        boolean [][] dp = new boolean[n+1][target+1];

        for (int i=0;i<=n;i++){
            dp[i][0] = true;
        }


        for (int i=1;i<=n;i++){
            for (int j=1;j<=target;j++){
                // choose i or not choose i
                if (j-nums[i-1]>=0) {
                    dp[i][j] = dp[i - 1][j] || dp[i - 1][j - nums[i - 1]];
                }
                else{
                    dp[i][j] = dp[i - 1][j];
                }
            }
        }

        return dp[nums.length][target];
    }
}