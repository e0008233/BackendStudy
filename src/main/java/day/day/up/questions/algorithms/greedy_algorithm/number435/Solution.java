package day.day.up.questions.algorithms.greedy_algorithm.number435;

import io.swagger.models.auth.In;

import java.util.Arrays;
import java.util.Comparator;

public class Solution {
    public int eraseOverlapIntervals(int[][] intervals) {
        if (intervals.length<=1) return 0;
        Arrays.sort(intervals, new Comparator<int[]>() {
            @Override
            public int compare(int[] o1, int[] o2) {
                return o1[0] - o2[0];
            }
        });

        int count = 0;
        int end = intervals[0][1];
        int keep = 0;
        for (int i = 0 ; i < intervals.length-1;i++){
            if (end>intervals[i+1][0]){
                count++;
                if (intervals[keep][1]>intervals[i+1][1]){
                    end = intervals[i+1][1];
                    keep = i+1;
                }
                else{
                    end = intervals[keep][1];
                }
            }
            else {
                end = intervals[i+1][1];
                keep = i+1;
            }
        }

        return count;


    }


}