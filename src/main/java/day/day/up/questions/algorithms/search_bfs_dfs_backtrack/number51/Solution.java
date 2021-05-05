package day.day.up.questions.algorithms.search_bfs_dfs_backtrack.number51;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Solution {
    public List<List<String>> solveNQueens(int n) {
        List<List<String>> ans = new ArrayList<>();
        String[][] state = new String[n][n];
        for (int i=0;i<n;i++){
            for (int j=0;j<n;j++){
                state[i][j]=".";
            }
        }
        for (int i = 0;i<n;i++) {
            backTracking(ans,state,i,0);
        }
        return ans;
    }
    private void backTracking(List<List<String>> ans, String[][] board, int row,int column){
        if (column<0||row<0||column>=board.length||row>=board.length){
            return;
        }

        if (isSafe(board,row,column)){
            board[row][column] = "Q";
            if (column==board.length-1){
                List<String> toAdd = new ArrayList<>();
                for (int i = 0;i<board.length;i++){
                    String temp="";
                    for (int j = 0;j<board.length;j++){
                        temp = temp + board[i][j];
                    }
                    toAdd.add(temp);
                }
                ans.add(toAdd);
            }
            else{
                for (int i = 0;i<board.length;i++) {
                    backTracking(ans,board,i,column+1);
                }
            }

            board[row][column] = ".";

        }

    }

    boolean isSafe(String board[][], int row, int col)
    {
        int i, j;

        /* Check this row on left side */
        for (i = 0; i < col; i++)
            if (board[row][i].equals("Q"))
                return false;

        /* Check upper diagonal on left side */
        for (i = row, j = col; i >= 0 && j >= 0; i--, j--)
            if (board[i][j].equals("Q"))
                return false;

        /* Check lower diagonal on left side */
        for (i = row, j = col; j >= 0 && i < board.length; i++, j--)
            if (board[i][j].equals("Q"))
                return false;

        return true;
    }
}