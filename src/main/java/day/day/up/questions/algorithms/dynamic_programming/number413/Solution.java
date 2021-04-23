package day.day.up.questions.algorithms.dynamic_programming.number413;


public class Solution {
    public int numberOfArithmeticSlices(int[] nums) {
        if (nums.length<=2) return 0;
        int[] currentResult = new int[nums.length];
        int result = 0;
        for (int i=2;i<nums.length;i++){
            if (nums[i]-nums[i-1]==nums[i-1]-nums[i-2]){
                currentResult[i]= currentResult[i-1]+1;
                result+=currentResult[i];
            }
        }
        return result;
    }
}