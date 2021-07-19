package day.day.up.questions.algorithms.no_topics.number238;

//https://www.cnblogs.com/ysw-go/p/11910406.html
class Solution {
    public int[] productExceptSelf(int[] nums) {
        int[] res = new int[nums.length];
        int start = 1;

        // product of the left
        for (int i=0;i<nums.length;i++){
            res[i] = start;
            start=start*res[i];
        }

        start=1;

        for (int i=nums.length-1;i>=0;i--){
            res[i] = start * res[i];
            start=start*nums[i];
        }

        return res;
    }

}