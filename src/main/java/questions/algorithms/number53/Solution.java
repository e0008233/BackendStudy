package questions.algorithms.number53;

// find the max ending at each array position
// time complexity O(n)
class Solution {
    public int maxSubArray(int[] nums) {
        if (nums.length<1) return 0;
        int max_so_far = nums[0];
        int max_ending_here = nums[0];
        for (int i =0; i < nums.length; i++){
            if (max_ending_here<0){
                max_ending_here = nums[i];
            }
            else {
                max_ending_here = max_ending_here + nums[i];
            }
            if (max_ending_here>max_so_far){
                max_so_far = max_ending_here;
            }
        }
        return max_so_far;
    }
}