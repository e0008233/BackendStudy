package day.day.up.questions.algorithms.topics.dynamic_programming.number918;


//https://blog.csdn.net/qq_17550379/article/details/82965510

public class Solution {
    public int maxSubarraySumCircular(int[] nums) {
        if (nums.length==1) return nums[0];

        int result =  nums[0];
        int minResult =   nums[0];
        int sum = 0;

        // case 1: non-circular
        int[] max_ending_here = new int[nums.length];
        // case 2: circular
        int[] min_ending_here = new int[nums.length];
        // case 3: all negative
        int min = Integer.MIN_VALUE;
        boolean hasPositive = result>=0 ?true:false;
        max_ending_here[0] = nums[0];
        min_ending_here[0] = nums[0];
        sum+=nums[0];
        for (int i=1;i<nums.length;i++){
            max_ending_here[i] = Math.max(max_ending_here[i - 1] + nums[i],nums[i]);
            result = Math.max(result, max_ending_here[i]);


            min_ending_here[i] = Math.min(min_ending_here[i-1]+ nums[i],nums[i]);
            minResult=Math.min(minResult,min_ending_here[i]);

            sum+=nums[i];
            if (nums[i]>0) hasPositive = true;
            min = Math.max(min,nums[i]);

        }
        if (hasPositive)
            return result>sum-minResult?result:sum-minResult;
        else
            return min;
    }
}