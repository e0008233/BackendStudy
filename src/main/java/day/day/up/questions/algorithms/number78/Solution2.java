package day.day.up.questions.algorithms.number78;

import java.util.ArrayList;
import java.util.List;


// https://leetcode.com/problems/subsets/solution/
// The idea is that we map each subset to a bitmask of length n,
// where 1 on the ith position in bitmask means the presence of nums[i] in the subset, and 0 means its absence.


// Generate all possible binary bitmasks of length n.
// Map a subset to each bitmask: 1 on the ith position in bitmask means the presence of nums[i] in the subset, and 0 means its absence.
// Return output list.

public class Solution2 {
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> output = new ArrayList();
        int n = nums.length;


        //  instead of    for (int i =0; i<(int)Math.pow(2, n);i++)  to solve zero left padding
        //  for example, n=3,   bitmask should be "000" instead of "0"
        //  hence use pow(2, n+1) shift it left

        for (int i = (int)Math.pow(2, n); i < (int)Math.pow(2, n + 1); ++i) {
            // generate bitmask, from 0..00 to 1..11
            String bitmask = Integer.toBinaryString(i).substring(1);

            // append subset corresponding to that bitmask
            List<Integer> curr = new ArrayList();
            for (int j = 0; j < n; ++j) {
                if (bitmask.charAt(j) == '1') curr.add(nums[j]);
            }
            output.add(curr);
        }
        return output;
    }
}