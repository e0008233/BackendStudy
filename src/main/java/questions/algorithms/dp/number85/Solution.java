package questions.algorithms.dp.number85;

// https://www.youtube.com/watch?v=g8bSdXCG-lA
// for each row,  treat it as the histogram and find the max, related to number84

import java.util.Stack;

public class Solution {
    public int maximalRectangle(char[][] matrix) {
        if (matrix.length==0||matrix[0].length==0) return 0;
        int row = matrix.length;
        int column = matrix[0].length;
        int[][] dp = new int[row][column];
        int[][] height = new int[row][column];
        int max = 0;
        for (int i=0;i<row;i++) {
            for (int j=0;j<column;j++){
                if (i==0) {
                    height[i][j] = matrix[i][j]=='1'?1:0;
                }
                else{
                    height[i][j] = matrix[i][j]=='1'?height[i-1][j]+1:0;
                }
            }
            int currentRowResult = largestRectangleArea(height[i]);
            if (currentRowResult>max) max= currentRowResult;
        }
        return max;
    }


    // see number84
    public int largestRectangleArea(int[] heights) {
        if (heights.length<=0) return 0;
        Stack<Integer> stack = new Stack<Integer>();
        int max = 0;
        int areaWithTop;
        for (int i =0; i<heights.length; i++){
            if (stack.isEmpty() || heights[i]>=heights[stack.peek()] ){
                stack.push(i);
            }else{
                int currentIndexToCalculate = i;
                while((!stack.isEmpty()) && heights[i] < heights[stack.peek()]){
                    currentIndexToCalculate = stack.pop(); //calculate rectangle for every popped value
                    int leftIndex;

                    if (stack.isEmpty()){
                        leftIndex=0; // current smallest value, rectangle needs to count from the start
                    }
                    else{
                        leftIndex = stack.peek() + 1;
                    }
                    int currentArea = (i-leftIndex)*heights[currentIndexToCalculate];
                    if (currentArea>max) max = currentArea;
                }

                stack.push(i);
            }
        }

        if (!stack.isEmpty()){
            int rightIndex = stack.peek();
            while (!stack.isEmpty()){
                int currentIndexToCalculate = stack.pop();
                int leftIndex = currentIndexToCalculate ;
                while (!stack.isEmpty() && heights[currentIndexToCalculate]==heights[stack.peek()]){
                    stack.pop();
                }
                if (stack.isEmpty()) leftIndex = 0;
                else{
                    leftIndex = stack.peek()+1;
                }
                int currentArea =  heights[currentIndexToCalculate] * (rightIndex-leftIndex+1);
                if (currentArea>max) max = currentArea;
            }
        }

        return max;
    }
}