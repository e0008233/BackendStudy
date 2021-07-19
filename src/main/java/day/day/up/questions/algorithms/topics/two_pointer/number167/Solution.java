package day.day.up.questions.algorithms.topics.two_pointer.number167;


public class Solution {
    public int[] twoSum(int[] numbers, int target) {
        int leftIndex = 0;
        int rightIndex = numbers.length-1;
        int[] result = new int[2];
        while(leftIndex<rightIndex){
            if(numbers[rightIndex]+numbers[leftIndex]<target){
                leftIndex++;
            }
            else if(numbers[rightIndex]+numbers[leftIndex]>target){
                rightIndex--;
            }
            else{
                result[0]=leftIndex+1;
                result[1]=rightIndex+1;
                break;
            }

        }
        return result;
    }
}