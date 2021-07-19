package day.day.up.questions.algorithms.topics.dynamic_programming.number494;


import java.util.HashMap;
import java.util.Map;

public class Solution {
    Map<Integer,Map<Integer,Integer>> resultMap;
    public int findTargetSumWays(int[] nums, int target) {
        resultMap = new HashMap<>();
        if (nums.length==0) return 0;

        int result = helper(nums,0,target,0);

        return result;
    }

    private int helper(int[] nums, int index, int target,int sum) {
        if (index>=nums.length){
            if (sum==target) return 1;
            else return 0;
        }
        if (resultMap.containsKey(index) && resultMap.get(index).containsKey(sum)) return resultMap.get(index).get(sum);

        return helper(nums,index+1,target,sum+nums[index]) + helper(nums,index+1,target,sum-nums[index]);


    }
}