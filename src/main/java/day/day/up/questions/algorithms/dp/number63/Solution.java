package day.day.up.questions.algorithms.dp.number63;

// find the max ending at each array position
// time complexity O(n)
class Solution {
    public int uniquePathsWithObstacles(int[][] obstacleGrid) {
        if (obstacleGrid.length<1||obstacleGrid[0].length<1) return 0;
        if (obstacleGrid[0][0]==1) return 0;
        if (obstacleGrid[obstacleGrid.length-1][obstacleGrid[0].length-1]==1) return 0;

        int numOfWays[][] = new int[obstacleGrid.length][obstacleGrid[0].length];
        for (int i=0;i<obstacleGrid.length;i++){
            for (int j=0;j<obstacleGrid[0].length;j++){
                if (obstacleGrid[i][j]==1){
                    numOfWays[i][j] = 0;
                }
                else {
                    if (i == 0 && j == 0){
                        numOfWays[i][j]=1;
                    }
                    else if (i == 0 || j == 0){
                        if ((i>0 && numOfWays[i-1][j]>0) || (j>0 && numOfWays[i][j-1]>0)){
                            numOfWays[i][j] = 1;                        }
                        else {
                            numOfWays[i][j] = 0;
                        }
                    }
                    else {
                        numOfWays[i][j] = numOfWays[i - 1][j] + numOfWays[i][j - 1];
                    }
                }
            }
        }


        return numOfWays[obstacleGrid.length-1][obstacleGrid[0].length-1];
    }
}