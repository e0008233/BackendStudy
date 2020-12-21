package questions.algorithms.dp.number64;

// find the max ending at each array position
// time complexity O(n)
class Solution {
    public int minPathSum(int[][] grid) {

        int minValue[][] = new int[grid.length][grid[0].length];

        for (int i=0;i<grid.length;i++) {
            for (int j = 0; j < grid[0].length; j++) {
                if (i==0 && j==0){
                    minValue[i][j]=grid[i][j];
                }
                else if (i==0 || j==0){
                    if (i>0){
                        minValue[i][j]=grid[i][j]+minValue[i-1][j];
                    }
                    else{
                        minValue[i][j]=grid[i][j]+minValue[i][j-1];
                    }
                }
                else {
                    minValue[i][j] = findmin(minValue[i - 1][j], minValue[i][j - 1]) + grid[i][j];
                }
            }
        }

        return minValue[grid.length-1][grid[0].length-1];

    }

    public int findmin(int m, int n){
        if (m>n) return n;
        else return m;
    }
}