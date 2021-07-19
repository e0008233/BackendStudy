package day.day.up.questions.algorithms.no_topics.number63;

public class Solution {
    public static int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if (obstacleGrid.length==0)
            return  0;

        int[][] result = new int[obstacleGrid.length][obstacleGrid[0].length];
        for (int i=0;i<obstacleGrid.length;i++){
            for (int j=0;j<obstacleGrid[0].length;j++){
                if (obstacleGrid[i][j]==1) result[i][j] = 0;
                else if (i==0 && j==0) result[i][j] =1;
                else if(i==0) result[i][j]= result[i][j-1];
                else if (j==0) result[i][j] = result[i-1][j];
                else result [i][j]= result[i-1][j]+result[i][j-1];
            }
        }
        return result[obstacleGrid.length-1][obstacleGrid[0].length-1];
    }
}