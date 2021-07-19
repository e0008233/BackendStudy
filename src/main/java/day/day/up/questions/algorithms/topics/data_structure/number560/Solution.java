package day.day.up.questions.algorithms.topics.data_structure.number560;

import java.util.HashMap;
import java.util.Map;


public class Solution {
    public int subarraySum(int[] nums, int k) {
        int currentSum = 0;
        int result = 0;
        Map<Integer,Integer> map = new HashMap<>();
        map.put(0,1);
        for (int num:nums){
            currentSum+=num;
            if (currentSum>=k) {
                result = result + map.get(currentSum - k);
            }
            if (map.containsKey(currentSum)){
                map.put(currentSum,map.get(currentSum)+1);
            }
            else{
                map.put(currentSum,1);
            }
        }


        return result;
    }
}