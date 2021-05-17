package day.day.up.questions.algorithms.divide_and_conquer.number312;

import java.util.ArrayList;
import java.util.List;
public class Solution {
    public Integer[][] dp;

    public int[] extended;
    public int maxCoins(int[] nums) {
        extended = new int[nums.length+2];


        dp = new Integer[extended.length][extended.length];

        extended[0] = 1;
        extended[nums.length+1] = 1;
        for (int i = 0;i<nums.length;i++){
            extended[i+1] = nums[i];
        }
        int result =  helper(1,nums.length);
        return result;
    }

    private int helper( int start, int end) {
        if (start>end) return 0;

        int result = 0;
        if (dp[start][end]!= null ) return dp[start][end];

        if (start==end){
            result =  extended[start-1]*extended[start]*extended[start+1];

            return result;
        }

        for (int i = start;i<=end;i++){
            // extended[i] is the last one to burst, hence extended[start-1]*extended[i]*extended[end+1]
            result = Math.max(result,helper(start,i-1)+helper(i+1,end)+extended[start-1]*extended[i]*extended[end+1]);
        }
        dp[start][end] = result;

        return result;
    }
}