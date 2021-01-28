package day.day.up.questions.algorithms.dac.number84;

//        Divide and Conquer
//        T(n) = O(Logn) + T(n-1), The solution of above recurrence is O(nLogn). So overall time is O(n) + O(nLogn) which is O(nLogn).
//        a) Maximum area in left side of minimum value (Not including the min value)
//        b) Maximum area in right side of minimum value (Not including the min value)
//        c) Number of bars multiplied by minimum value.
public class Solution {
    public int largestRectangleArea(int[] heights) {
        if (heights.length<=0) return 0;


        int result = dac(0,heights.length-1,heights);
        return result;
    }

    public int dac(int start, int end, int[] heights){
        if (end<start) return 0;
        if (end==start) return heights[end];

        int minimumMid = heights[start];
        int minimumIndex = start;
        for (int i = start; i <=end; i++){
            if (heights[i]<minimumMid){
                minimumMid = heights[i];
                minimumIndex = i;
            }
        }

        int leftArea = dac(start,minimumIndex-1,heights);
        int rightArea = dac(minimumIndex+1,end,heights);
        int middleArea = heights[minimumIndex]*(end-start+1);

        return max(leftArea,rightArea,middleArea);
    }

    public int max(int x, int y, int z){
        if (x>y){
            if (z>x) return z;
            else return x;
        }else{
            if (z>y) return z;
            else return y;
        }
    }
}