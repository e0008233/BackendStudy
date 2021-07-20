package day.day.up.questions.algorithms.topics.data_structure.graph.number785;


import java.util.LinkedList;
import java.util.Queue;

public class Solution {
    public boolean isBipartite(int[][] graph) {
        Queue<Integer> queue = new LinkedList<>();
        int n = graph.length;
        int[] visited = new int[n];
        int[] parts = new int[n];

        int partsNumber = 1;
        for (int i = 0;i<n;i++){
            if (visited[i]!=0) continue;  //visited

            queue.add(i);

            while (!queue.isEmpty()){
                int curr = queue.remove();
                if (visited[curr]==0) {
                    parts[curr] = partsNumber;
                    visited[curr] = 1;
                }
                for (int j=0;j<graph[curr].length;j++){
                    int neighbour = graph[curr][j];
                    if (visited[neighbour]==0){
                        visited[neighbour] =1;
                        parts[neighbour] = -1/parts[curr];
                        queue.add(neighbour);
                    }else{
                        if (parts[neighbour]*parts[curr] != -1) return false;
                    }
                }
            }
        }

        return true;
    }
}