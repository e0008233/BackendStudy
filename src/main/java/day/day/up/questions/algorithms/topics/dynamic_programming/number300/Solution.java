package day.day.up.questions.algorithms.topics.dynamic_programming.number300;


public class Solution {
    int[] resultEndingHere;
    public int lengthOfLIS(int[] nums) {
        if (nums.length==0) return 0;
        if (nums.length==1) return 1;
        resultEndingHere = new int[nums.length];
        resultEndingHere[0] = 1;
        int max = 0;
        for (int i=1;i<nums.length;i++){
            int result = 1;
            for (int j=0;j<i;j++){
                if (nums[i]>nums[j]) result=Math.max(result,resultEndingHere[j]+1);
            }
            resultEndingHere[i] = result;
            if (result>max) max= result;
        }

        return max;
    }
}