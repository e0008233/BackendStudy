package day.day.up.questions.algorithms.search_bfs_dfs_backtrack.number934;

import com.sun.xml.internal.bind.v2.runtime.Coordinator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

// multi-source, multi-destination shortest path
// use dfs to find one island (connected component)
// mark all nodes of that island to 2
// use bfs to expand one island (marking as 2 ), push all to queue until another one is reached
public class Solution {
    class Coordinates{
        public int i;
        public int j;

        public Coordinates(int i, int j){
            this.i=i;
            this.j=j;
        }
    }
    public static Queue<Coordinates> queue = new LinkedList<>();
    public static int [] directions = new int[]{0,1,0,-1,0};
    public int shortestBridge(int[][] A) {
        queue = new LinkedList<>();
        directions = new int[]{0,1,0,-1,0};
        boolean isFound = false;
        for (int i=0;i<A.length && !isFound;i++){
            for (int j=0;j<A[0].length && !isFound;j++){
                if (A[i][j]>0){
                    isFound = true;
                    dfs(A,i,j);
                }
            }
        }
        int result = 0;
        while (!queue.isEmpty()){
            int size = queue.size();

            for (int i = 0; i<size; i++){
                Coordinates coordinates = queue.remove();
                for (int j=0;j<4;j++){
                    // 4 directions
                    int x = coordinates.i+directions[j];
                    int y = coordinates.j+directions[j+1];

                    if (x<0 || y< 0|| x>=A.length||y>=A[0].length||A[x][y]==2) continue;

                    if (A[x][y]==0){
                        A[x][y]=2;
                        queue.add(new Coordinates(x,y));
                    }
                    else if (A[x][y]==1){
                        return result;
                    }
                }
            }

            result++;
        }

        return -1;
    }


    private void dfs(int[][] grid, int i, int j) {
        if (i<0 || j< 0|| i>=grid.length||j>=grid[0].length||grid[i][j]==0||grid[i][j]==2) return;
        grid[i][j] = 2;
        Coordinates coordinates = new Coordinates(i,j);
        queue.add(coordinates);
        dfs(grid,i+1,j);
        dfs(grid,i-1,j);
        dfs(grid,i,j+1);
        dfs(grid,i,j-1);
    }
}