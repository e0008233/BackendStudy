package day.day.up.questions.algorithms.dp.number62;

// find the max ending at each array position
// time complexity O(n)
class Solution {
    public int uniquePaths(int m, int n) {
        if (n<=0 || m<=0) return 0;

        int numOfWays[][] = new int[m][n];
        for (int i=0;i<m;i++){
            for (int j=0;j<n;j++){
                if (i==0||j==0) numOfWays[i][j]=1;
                else {
                    numOfWays[i][j]= numOfWays[i-1][j] + numOfWays[i][j-1];
                }
            }
        }
        return numOfWays[m-1][n-1];
    }
}