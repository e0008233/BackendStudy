package day.day.up.questions.algorithms.search_bfs_dfs.number695;

class Solution {
    public int maxAreaOfIsland(int[][] grid) {
        if (grid.length==0||grid[0].length==0) return 0;

        int max = 0;
        for (int i=0;i<grid.length;i++){
            for (int j=0;j<grid[0].length;j++){
                if (grid[i][j]>0){
                    int area = 0;
                    grid[i][j]=0;// mark as visited
                    area =dfs(grid,i,j);

                    if (area>max) max = area;
                }
            }
        }


        return max;
    }


    private int dfs(int[][] grid , int i, int j){
        if (i<0 || j< 0|| i>=grid.length||j>=grid[0].length||grid[i][j]==0) return 0;

        grid[i][j]=0; //mark as visited
        return 1 + dfs(grid,i+1,j)+ dfs(grid,i-1,j)+ dfs(grid,i,j+1)+ dfs(grid,i,j-1);

    }
}