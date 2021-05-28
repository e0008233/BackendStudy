package day.day.up.questions.algorithms.bit_operation.number461;

public class Solution {
    public int hammingDistance(int x, int y) {
        int ans = 0;
        int diff = x^y;

        // check how many 1s in diff in binary form
        while (diff>0){
            ans = ans + (diff & 1);
            diff = diff>>1;
        }
        return ans;
    }
}