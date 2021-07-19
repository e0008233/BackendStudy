package day.day.up.questions.algorithms.no_topics.number56;

import java.util.*;

public class Solution {
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