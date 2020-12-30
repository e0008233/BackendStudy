package questions.algorithms.dac.number84;

//        Divide and Conquer
//        T(n) = O(Logn) + T(n-1), The solution of above recurrence is O(nLogn). So overall time is O(n) + O(nLogn) which is O(nLogn).
//        a) Maximum area in left side of minimum value (Not including the min value)
//        b) Maximum area in right side of minimum value (Not including the min value)
//        c) Number of bars multiplied by minimum value.
public class Solution {
    public int largestRectangleArea(int[] heights) {
        if (heights.length<=0) return 0;


        for (int i = 0; i < heights.length; i++){

        }
        return 0;
    }

    public int dac(int[] heights){
        int minimumMid = heights[0];
        int minimumIndex = 0;
        for (int i = 0; i < heights.length; i++){
            if (heights[i]>minimumMid){
                minimumMid = heights[i];
                minimumIndex = minimumIndex;
            }
        }

        return 0;
    }
}