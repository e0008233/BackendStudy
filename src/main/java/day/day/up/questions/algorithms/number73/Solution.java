package day.day.up.questions.algorithms.number73;

public class Solution {
    public void setZeroes(int[][] matrix) {
        boolean isFirstColumnZero = false;
        boolean isFirstRowZero = false;

        // first column
        for (int i = 0;i<matrix.length;i++) {
            if (matrix[i][0]==0) isFirstColumnZero=true;
        }

        // first row
        for (int j=0; j<matrix[0].length;j++){
            if (matrix[0][j]==0) isFirstRowZero=true;

        }
        for (int i = 1;i<matrix.length;i++){
            for (int j=1; j<matrix[0].length;j++){
                if (matrix[i][j]==0){
                    matrix[i][0]=0;
                    matrix[0][j]=0;
                }
            }
        }

        for (int i = 1;i<matrix.length;i++) {
            for (int j = 1; j < matrix[0].length; j++) {
                if (matrix[0][j] == 0 || matrix[i][0] == 0) {
                    matrix[i][j] = 0;
                }
            }
        }
        // first column
        if (isFirstColumnZero){
            for (int i = 0;i<matrix.length;i++) {
                matrix[i][0]=0;
            }
        }

        // first row
        if (isFirstRowZero){
            for (int j=0; j<matrix[0].length;j++){
                matrix[0][j]=0;
            }
        }
    }
}