package day.day.up.questions.algorithms.dynamic_programming.number198;

// Create an extra space dp, DP array to store the sub-problems.
// Tackle some basic cases, if the length of the array is 0, print 0, if the length of the array is 1, print the first element, if the length of the array is 2, print maximum of two elements.
// Update dp[0] as array[0] and dp[1] as maximum of array[0] and array[1]
// Traverse the array from the second element to the end of array.
// For every index, update dp[i] as maximum of dp[i-2] + array[i] and dp[i-1], this step defines the two cases, if an element is selected then the previous element cannot be selected and if an element is not selected then the previous element can be selected.
// Print the value dp[n-1]
public class Solution {
    public int rob(int[] nums) {
        if (nums.length==0) return 0;
        if (nums.length==1) return nums[0];
        if (nums.length==2) return Math.max(nums[0],nums[1]);
        int[] result = new int[nums.length];
        result[0] = nums[0];
        result[1] = Math.max(nums[0],nums[1]);
        for (int i=2;i<nums.length;i++){
            result[i] = Math.max(result[i-2]+nums[i],result[i-1]);
        }
        return result[nums.length-1];
    }
}