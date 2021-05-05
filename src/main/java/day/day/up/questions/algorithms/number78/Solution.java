package day.day.up.questions.algorithms.number78;

import java.util.ArrayList;
import java.util.List;


// start from the empty list
// creating and adding new element to all existing list

public class Solution {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        ans.add(new ArrayList<Integer>());

        for (int num: nums){
            List<List<Integer>> toAdd = new ArrayList<>();
            for (List<Integer> list:ans){
                List<Integer> copy = new ArrayList<>(list);
                copy.add(num);
                toAdd.add(copy);
            }

            for (List<Integer> list:toAdd){
                ans.add(list);
            }
        }
        return ans;

    }
}