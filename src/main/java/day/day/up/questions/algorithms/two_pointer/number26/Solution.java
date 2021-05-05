package day.day.up.questions.algorithms.two_pointer.number26;


public class Solution {
    public int removeDuplicates(int[] nums) {
        if (nums.length==0) return 0;
        if (nums.length==1) return 1;
        int slow = 0;
        int fast = slow+1;
        int result = 0;
        while (fast<nums.length){
            if (nums[result]==nums[slow+1]){
                while (fast<nums.length){
                    if (nums[slow]!=nums[fast]){
                        nums[result+1]=nums[fast];
                        slow=fast;
                        fast++;
                        result++;
                        break;
                    }
                    else{
                       fast++;
                    }
                }
            }
            else{
                nums[result+1]=nums[slow+1];
                result++;
                slow++;
                fast++;
            }
        }
        return result+1;
    }
}