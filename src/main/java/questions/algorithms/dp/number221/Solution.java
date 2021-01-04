package questions.algorithms.dp.number221;


// https://leetcode.com/problems/maximal-square/
// dp(i,j) represents the side length of the maximum square whose bottom right corner is the cell with index (i,j) in the original matrix.
// dp(i,j)=min(dp(i−1,j),dp(i−1,j−1),dp(i,j−1))+1

public class Solution {
    public int maximalSquare(char[][] matrix) {
        if (matrix.length==0||matrix[0].length==0) return 0;
        int result = 0;
        int[][] dp = new int[matrix.length][matrix[0].length];
        for (int i=0; i<matrix.length;i++){
            for (int j=0; j < matrix[0].length; j++){
                if (i==0||j==0) dp[i][j] = Character.getNumericValue(matrix[i][j]);
                else if (matrix[i][j] == '0'){
                    dp[i][j]= 0;
                }
                else{
                    dp[i][j] = Math.min(Math.min(dp[i-1][j-1],dp[i-1][j]), dp[i][j-1])+1;
                }
                if (dp[i][j]>result) result=dp[i][j];
            }

        }
        return result*result;

    }
}