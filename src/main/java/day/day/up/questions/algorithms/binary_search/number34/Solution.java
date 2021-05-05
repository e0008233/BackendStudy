package day.day.up.questions.algorithms.binary_search.number34;

public class Solution {
    public int[] searchRange(int[] nums, int target) {

        if (nums.length==0) return new int[]{-1,-1};
        if (nums.length==1) return nums[0]==target?new int[]{0,0}:new int[]{-1,-1};

        int start = 0;

        int end = nums.length-1;

        while (start<=end){
            int mid = (start+end)/2;
            if (nums[mid]==target){
                int leftBound = findLeftBound(nums,target,mid);
                int rightBound = findRightBount(nums,target,mid);
                return new int[]{leftBound,rightBound};
            }
            else if (nums[mid]>target){
                end=mid-1;
            }
            else{
                start=mid+1;
            }
        }
        return new int[]{-1,-1};


    }

    private int findRightBount(int[] nums, int target, int mid) {
        while (mid<nums.length-1){
            mid++;
            if (nums[mid]!=target) return mid-1;
        }
        return mid;
    }

    private int findLeftBound(int[] nums, int target, int mid) {
        while (mid>0){
            mid--;
            if (nums[mid]!=target) return mid+1;
        }
        return mid;
    }
}