package day.day.up.questions.algorithms.topics.dynamic_programming.number542;

import java.util.Arrays;

import static java.lang.Math.min;

class Solution {
    public int[][] updateMatrix(int[][] mat) {

        int rows = mat.length;
        int cols = mat[0].length;

        int[][] dist = new int[rows][cols];
        for (int[] temp: dist) Arrays.fill(temp, Integer.MAX_VALUE-10000);

        //First pass: check for left and top (if nearest 0 is on the left or top)
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (mat[i][j] == 0)
                    dist[i][j] = 0;
                else {
                    if (i > 0)
                        dist[i][j] = Math.min(dist[i][j], dist[i - 1][j] + 1);
                    if (j > 0)
                        dist[i][j] = Math.min(dist[i][j], dist[i][j - 1] + 1);
                }
            }
        }

        //Second pass: check for bottom and right (if nearest 0 is on the right or bottom)
        for (int i = rows - 1; i >= 0; i--) {
            for (int j = cols - 1; j >= 0; j--) {
                if (i < rows - 1)
                    dist[i][j] = Math.min(dist[i][j], dist[i + 1][j] + 1);
                if (j < cols - 1)
                    dist[i][j] = Math.min(dist[i][j], dist[i][j + 1] + 1);
            }
        }
        return dist;
    }
}