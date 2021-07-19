package day.day.up.questions.algorithms.topics.two_pointer.number15;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        if (nums.length==0) return ans;
        Arrays.sort(nums);
        for (int i = 0; i<nums.length-2;i++){
            int twoSum = 0 - nums[i];

            // ignore duplicates
            if (i>0 && nums[i] == nums[i-1]) continue;

            // two pointer to find the pair
            int leftPointer = i+1;
            int rightPointer = nums.length-1;
            while (leftPointer<rightPointer){
                int sum = nums[leftPointer] + nums[rightPointer];
                if (sum==twoSum){
                    List<Integer> toAdd = new ArrayList<>();
                    toAdd.add(nums[i]);
                    toAdd.add(nums[leftPointer]);
                    toAdd.add(nums[rightPointer]);
                    ans.add(toAdd);
                    int tempLeft = leftPointer;
                    leftPointer++;
                    while(leftPointer<=rightPointer && nums[tempLeft] == nums [leftPointer]){
                        leftPointer++;
                    }
                } else if (sum<twoSum) {
                    leftPointer++;
                }
                else{
                    rightPointer--;
                }
            }
        }
        return ans;
    }
}