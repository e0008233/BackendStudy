package day.day.up.questions.algorithms.sorting.number215;


public class Solution {
    public int findKthLargest(int[] nums, int k) {
        int start = 0;
        int end = nums.length-1;
        int result= quickSort(nums,start,end)+1;
        while (result!=k){
            if (k>result+1){
                start = result+1;
            }
            else{
                end = result-1;
            }
            result=quickSort(nums,start,end);
        }
        return nums[result];
    }

    public int quickSort(int[] nums, int start, int end){
        if (start>=end) return -1;
        int pivot = nums[end];
        int index = start-1;

        while(start<=end-1){
            if (nums[start]<end){
                index++;
                swap(nums,index,start);
            }
            start++;
        }
        index++;
        swap(nums,index,end);

        return index;
    }

    private void swap(int[] nums, int index, int start) {
        int temp = nums[index];
        nums[index] = nums[start];
        nums[start] = temp;
    }

}