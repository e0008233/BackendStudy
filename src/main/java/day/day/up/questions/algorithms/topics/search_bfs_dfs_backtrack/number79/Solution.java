package day.day.up.questions.algorithms.topics.search_bfs_dfs_backtrack.number79;

public class Solution {
    public static boolean[][] isVisited;
    public boolean exist(char[][] board, String word) {
        int m = board.length;
        int n = board[0].length;
        isVisited = new boolean[m][n];
        for (int i = 0; i<m; i++){
            for (int j=0;j<n;j++){

                boolean isFound = bfs(board,word,0,i,j);
                if (isFound) return true;
            }
        }

        return false;

    }

    private boolean bfs(char[][] board, String word, int index, int i, int j) {
        if (i<0 || j< 0|| i>=board.length||j>=board[0].length||index>=word.length()||isVisited[i][j]) return false;
        if (board[i][j]!=word.charAt(index)) return false;
        if (index==word.length()-1) return true;

        isVisited[i][j] = true;
        if (bfs(board,word,index+1,i-1,j)) return true;
        if (bfs(board,word,index+1,i+1,j)) return true;
        if (bfs(board,word,index+1,i,j-1)) return true;
        if (bfs(board,word,index+1,i,j+1)) return true;

        isVisited[i][j] = false;
        return false;

    }
}