package day.day.up.questions.algorithms.search_bfs_dfs_backtrack.number417;

import java.util.ArrayList;
import java.util.List;

class Solution {
    public static boolean[][] isToPO ;
    public static boolean[][] isToAO;
    public static boolean[][] isPOVisited;
    public static boolean[][] isAOVisited;
    public List<List<Integer>> pacificAtlantic(int[][] heights) {
        List<List<Integer>> result = new ArrayList<>();
        isToPO = new boolean[heights.length][heights[0].length];
        isToAO = new boolean[heights.length][heights[0].length];

        isPOVisited = new boolean[heights.length][heights[0].length];
        isAOVisited = new boolean[heights.length][heights[0].length];


        for (int i=0;i<heights.length;i++){
            for (int j=0;j<heights[0].length;j++){
              if (i==0 || j==0){
                  dfsPO(heights,i,j,0);
              }

              if (i==heights.length-1 || j==heights[0].length-1){
                  dfsAO(heights,i,j,0);
              }

            }
        }

        for (int i=0;i<heights.length;i++){
            for (int j=0;j<heights[0].length;j++){
                if (isToAO[i][j] && isToPO[i][j]) {
                    List<Integer> toAdd = new ArrayList<>();
                    toAdd.add(i);
                    toAdd.add(j);
                    result.add(toAdd);
                }
            }
        }
        return result;

    }


    private void dfsPO(int[][] height , int i, int j, int currentHeight){
        if (i<0 || j< 0|| i>=height.length||j>=height[0].length ||isToPO[i][j]) return;

        if (i==0 || j==0 || height[i][j]>=currentHeight){
            isToPO[i][j] = true;
            dfsPO(height,i+1,j,height[i][j]);
            dfsPO(height,i-1,j,height[i][j]);
            dfsPO(height,i,j+1,height[i][j]);
            dfsPO(height,i,j-1,height[i][j]);

        }
        else{
            return;
        }

    }



    private void dfsAO(int[][] height , int i, int j, int currentHeight){
        if (i<0 || j< 0|| i>=height.length||j>=height[0].length||isToAO[i][j]) return;

        if (i==height.length-1 || j==height[0].length-1 || height[i][j]>=currentHeight){
            isToAO[i][j] = true;
            dfsAO(height,i+1,j,height[i][j]);
            dfsAO(height,i-1,j,height[i][j]);
            dfsAO(height,i,j+1,height[i][j]);
            dfsAO(height,i,j-1,height[i][j]);

        }
        else{
            return;
        }

    }
}