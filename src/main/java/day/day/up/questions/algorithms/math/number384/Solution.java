package day.day.up.questions.algorithms.math.number384;

import java.util.Arrays;
import java.util.Random;


class Solution {

    int[] originalNums;
    int[] currNums;
    Random random;

    public Solution(int[] nums) {
        originalNums= nums.clone();
        currNums=nums.clone();
        this.random = new Random(System.currentTimeMillis());

    }

    /** Resets the array to its original configuration and return it. */
    public int[] reset() {
        return originalNums;
    }

    /** Returns a random shuffling of the array. */
    public int[] shuffle() {
        for(int i = 0; i < currNums.length; i++) {
            int tmp = currNums[i];
            int idx = random.nextInt(currNums.length - i) + i;
            currNums[i] = currNums[idx];
            currNums[idx] = tmp;
        }
        return currNums;
    }
}

/**
 * Your Solution object will be instantiated and called as such:
 * Solution obj = new Solution(nums);
 * int[] param_1 = obj.reset();
 * int[] param_2 = obj.shuffle();
 */