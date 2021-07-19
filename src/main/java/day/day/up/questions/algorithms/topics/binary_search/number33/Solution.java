package day.day.up.questions.algorithms.topics.binary_search.number33;

public class Solution {
    public int search(int[] nums, int target) {
        if (nums.length==0) return -1;
        if (nums.length==1) return nums[0]==target?0:-1;

        int start = 0;
        int end = nums.length-1;

        while (start<end){
            int mid = (start+end)/2;

            if (nums[mid] == target)return mid;
            if (nums[start]==target )return start;
            if (nums[end]==target) return end;

            if (nums[start]<nums[mid]){
                // left side sorted
                if (target<nums[mid]&&target>nums[start]){
                    end=mid-1;
                }
                else{
                    start=mid+1;
                }
            }
            else{
                //right side sorted
                if (target>nums[mid]&&target<nums[end]){
                    start=end+1;
                }
                else{
                    end=mid-1;
                }
            }
        }
        return -1;

    }
}