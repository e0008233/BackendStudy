package day.day.up.questions.algorithms.no_topics.number57;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Solution {
    public int[][] insert(int[][] intervals, int[] newInterval) {
        int [][] ans = new int[intervals.length+1][2];
        for (int i=0;i<intervals.length;i++){
            ans[i] = intervals[i];
        }

        ans[intervals.length] = newInterval;

        return merge(ans);

    }


    public int[][] merge(int[][] intervals) {
        Arrays.sort(intervals,new myComparator());
        List<int []> ans = new ArrayList<>();
        ans.add(intervals[0]);

        for (int i = 1; i<intervals.length;i++){
            if (ans.get(ans.size()-1)[1] >= intervals[i][0]){
                if (ans.get(ans.size()-1)[1] < intervals[i][1]) ans.get(ans.size()-1)[1]= intervals[i][1];
            }
            else{
                ans.add(intervals[i]);
            }
        }

        int [][] toReturn = new int[ans.size()][2];

        for (int i = 0; i<ans.size();i++){
            toReturn[i] = ans.get(i);
        }

        return toReturn;
    }


    class myComparator implements Comparator<int[]> {

        @Override
        public int compare(int[] o1, int[] o2) {
            return o1[0]-o2[0];
        }
    }
}