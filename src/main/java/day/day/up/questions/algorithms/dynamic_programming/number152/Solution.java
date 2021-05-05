package day.day.up.questions.algorithms.dynamic_programming.number152;



public class Solution {
    public int maxProduct(int[] nums) {
        // Assumption: nums.length>=1

        int maxValue = nums[0];

        int minEndingBefore = nums[0]; // for negative value before
        int maxEndingBefore = nums[0];
        for (int i=1;i<nums.length;i++){
            int value1 = nums[i]*maxEndingBefore;
            int value2 = nums[i]*minEndingBefore;
            minEndingBefore = Math.min(Math.min(nums[i],value1),value2);
            maxEndingBefore = Math.max(Math.max(nums[i],value1),value2);;

            maxValue=Math.max(Math.max(minEndingBefore,maxEndingBefore),maxValue);
        }
        return maxValue;
    }
}