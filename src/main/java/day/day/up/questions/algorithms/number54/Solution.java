package day.day.up.questions.algorithms.number54;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    List<Integer> ans;
    int row;
    int column;
    public List<Integer> spiralOrder(int[][] matrix) {
        ans = new ArrayList<>();

        Boolean[][] isVisited = new Boolean[matrix.length][matrix[0].length];

        // 0: right; 1:down; 2: left, 3: up
        int direction = 0;
        row = 0;
        column = 0;
        int count = 0;
        while (count<matrix.length*matrix[0].length){
            count++;
            direction=direction%4;

            if (direction==0){
                moveRight(matrix,isVisited);
            }
            else if (direction==1){
                moveDown(matrix,isVisited);
            }
            else if (direction==2){
                moveLeft(matrix,isVisited);
            }
            else if (direction==3){
                moveUp(matrix,isVisited);
            }
            direction++;
        }

        return ans;
    }

    private void moveLeft(int[][] matrix, Boolean[][] isVisited) {
        while (column>=0&&isVisited[row][column]==null){
            isVisited[row][column]=true;
            ans.add(matrix[row][column]);
            column--;
        }
        column++;
        row--;
    }

    private void moveUp(int[][] matrix, Boolean[][] isVisited) {
        while (row>=0&&isVisited[row][column]==null){
            isVisited[row][column]=true;
            ans.add(matrix[row][column]);
            row--;
        }
        row++;
        column++;
    }

    private void moveDown(int[][] matrix, Boolean[][] isVisited) {
        while (row<matrix.length&&isVisited[row][column]==null){
            isVisited[row][column]=true;
            ans.add(matrix[row][column]);
            row++;
        }
        row--;
        column--;
    }

    private void moveRight(int[][] matrix, Boolean[][] isVisited) {
        while (column<matrix[0].length&&isVisited[row][column]==null){
            isVisited[row][column]=true;
            ans.add(matrix[row][column]);
            column++;
        }
        column--;
        row++;
    }
}