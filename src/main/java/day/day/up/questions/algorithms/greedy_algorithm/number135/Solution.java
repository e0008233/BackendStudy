package day.day.up.questions.algorithms.greedy_algorithm.number135;

import java.util.Arrays;

public class Solution {
    public int candy(int[] ratings) {
        if(ratings.length<=0) return 0;
        if(ratings.length==1) return 1;
        int[] result = new int[ratings.length];
        Arrays.fill(result,1);
        for (int i=0;i<ratings.length-1;i++){
            if(ratings[i]<ratings[i+1]){
                result[i+1]=result[i]+1;
            }
        }

        for (int i=ratings.length-1;i>0;i--){
            if(ratings[i]<ratings[i-1] && result[i-1]<=result[i]){
                result[i-1]=result[i]+1;
            }
        }

        return Arrays.stream(result).sum();
    }
}