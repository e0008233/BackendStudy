package day.day.up.questions.algorithms.sorting.number347;



import java.util.HashMap;

public class Solution {
    public int[] topKFrequent(int[] nums, int k) {
        int[] result = new int[k];
        HashMap<Integer, Integer> counter = new HashMap<>();

        for (int i:nums){
            counter.merge(i,1,Integer::sum);
        }
        return result;
    }
}