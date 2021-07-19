package day.day.up.questions.algorithms.topics.greedy_algorithm.number455;

import java.util.Arrays;

public class Solution {
    public int findContentChildren(int[] g, int[] s) {
        if (g.length<=0 || s.length<=0) return 0;

        Arrays.sort(g);
        Arrays.sort(s);
        int gIndex = 0;
        int sIndex = 0;

        while(gIndex<g.length && sIndex<s.length){
            sIndex++;
            if (g[gIndex]<=s[sIndex]) {
                gIndex++;
            }
        }

        return gIndex;
    }
}