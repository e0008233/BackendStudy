package day.day.up.questions.algorithms.dynamic_programming.number256;


// dp[i][j] will denote the minimum cost of painting till house i and painint the ith house with jth color.
// dp[i][j] = cost[i][j]+min(cost[i-1][x]) for all x where x!=j.
// dp[i][0] = cost[i][0] + min(cost[i-1][1], cost[i-1][2])
// dp[i][1] = cost[i][1] + min(cost[i-1][0], cost[i-1][2])
// dp[i][2] = cost[i][2] + min(cost[i-1][0], cost[i-1][1])
public class Solution {
    public int minCost(int[][] costs) {
        if (costs.length==0) return 0;
        int minCost = Integer.MAX_VALUE;
        int[][] totalCosts = new int[costs.length][costs[0].length];

        for (int i = 0;i<costs.length; i++){
            for (int j = 0; j < costs[0].length; j++){
                if (i==0){
                    totalCosts[i][j] = costs[i][j];
                }
                else{
                    totalCosts[i][j] = costs[i][j] + Math.min(totalCosts[i-1][(j+1)%3],totalCosts[i-1][(j+2)%3]);
                }
            }
        }

        for (int i =0; i<totalCosts[costs.length-1].length;i++){
            if (totalCosts[costs.length-1][i]<minCost) minCost = totalCosts[costs.length-1][i];
        }
        return minCost;
    }

}