package day.day.up.questions.algorithms.search_bfs_dfs.number547;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Solution {
    public int findCircleNum(int[][] isConnected) {
        int count = 0;
        int size = isConnected.length;
        for (int i = 0;i<size;i++){

            for (int j = 0; j< isConnected.length; j++){
                if (isConnected[i][j] == 1){
                    dfs(isConnected,i,j);
                    count++;
                }
            }
        }

        return count;

    }

    private void dfs(int[][] isConnected , int i, int j){
        isConnected[i][j] = 0;  //mark as visited
        isConnected[j][i] = 0;
        for (int k=0;k<isConnected.length;k++){
            if (isConnected[i][k]==1){ // visit all i's friends
                dfs(isConnected,i,k);
            }
            if (isConnected[j][k]==1){ // visit all j's friends
                dfs(isConnected,j,k);
            }
        }

    }
}