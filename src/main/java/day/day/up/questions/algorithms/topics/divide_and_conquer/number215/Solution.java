package day.day.up.questions.algorithms.topics.divide_and_conquer.number215;

// quick sort: return the correct pivot
public class Solution {
    public int findKthLargest(int[] nums, int k) {
        int first = 0;
        int last = nums.length-1;
        int index = partition(first,last,nums);

        // find kth largest, not the smallest
        int m = nums.length-k;
        while (index!=m){
            if (index<m){
                first=index+1;
                index = partition(first,last,nums);
            }
            else{
                last=index-1;
                index = partition(first,last,nums);
            }
        }

        return nums[index];
    }

    public int partition(int first, int last,int[] nums){
        if (first>last) return first;
        if (first == last) return last;
        int indexForSmallElement = first-1;

        // take the last element as the pivot
        for (int i= first;i<=last-1;i++){
            if (nums[i]<nums[last]){
                indexForSmallElement++;

                //swap
                int temp = nums[indexForSmallElement];
                nums[indexForSmallElement] = nums[i];
                nums[i] = temp;
            }
        }

        // swap the pivot
        int temp2 = nums[indexForSmallElement+1];
        nums[indexForSmallElement+1] = nums[last];
        nums[last] = temp2;

        return indexForSmallElement+1;
    }
}
