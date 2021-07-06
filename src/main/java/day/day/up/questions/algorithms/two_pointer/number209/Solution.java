package day.day.up.questions.algorithms.two_pointer.number209;


public class Solution {
    public int minSubArrayLen(int target, int[] nums) {
        return findShortestLength(nums,target);
    }
    public int findShortestLength(int [] number, int target){
        if (number.length==0) return 0;


        int start = 0;
        int newStart= 0;
        int currentSum = number[0];
        int minLength = Integer.MAX_VALUE;

        if (currentSum>=target) return 1;


        for (int i=1;i<number.length;i++){
            currentSum = currentSum + number[i];
            if (currentSum>=target){
                // new start window
                newStart = getShortestStart(number,target,start,i,currentSum);

                int length = i-newStart+1;
                if (length<minLength) minLength= length;


                // modify the sum
                for (int j =start;j<newStart;j++){
                    currentSum= currentSum - number[j];
                }
                start = newStart;
            }


        }
        if (minLength==Integer.MAX_VALUE) return 0;

        return minLength;
    }

    private static int getShortestStart(int[] number, int target, int start,int end,int currentSum) {
        int i = start;
        for (i = start;i<=end;i++){
            currentSum= currentSum-number[i];
            if (currentSum<target){
                break;
            }
        }

        return i;
    }
}