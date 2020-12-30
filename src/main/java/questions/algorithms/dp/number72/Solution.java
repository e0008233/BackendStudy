package questions.algorithms.dp.number72;
//1. If first characters of two strings are same, nothing much to do. Ignore first characters and get count for remaining strings. So we recur for lengths m-1 and n-1.
//2. Else (If first characters are not same), we consider all operations on ‘str1’, consider all three operations on last character of first string, recursively compute minimum cost for all three operations and take minimum of three values.
//        Insert: Recur for m and n+1
//        Remove: Recur for m+1 and n
//        Replace: Recur for m+1 and n+1

public class Solution {
    Integer result[][];
    public int minDistance(String word1, String word2) {

        result = new Integer[word1.length()+1][word2.length()+1];

        int finalResult = dp(0,0, word1,word2);
        return finalResult;
    }

    public int dp (int i, int j, String word1, String word2){
        int currentResult = 0;
        if (result[i][j]!=null) return result[i][j];

        // if word1 is empty, add all word2 letters
        if (word1.length()==i){
            currentResult = word2.length()-j;
        }
        // if word2 is empty, remove all word1 letters
        else if(word2.length()==j){
            currentResult = word1.length()-i;
        }

        else if (word1.charAt(i)==word2.charAt(j)){
            currentResult = dp(i+1,j+1,word1,word2);
        }
        else{
            currentResult = 1 + min (dp(i+1,j+1,word1,word2), // replace the first one
                    dp(i,j+1,word1,word2),                   // add the first letter in word1
                    dp(i+1, j, word1, word2));               // remove the first letter in word1
        }
        result[i][j] = currentResult;
        return currentResult;
    }

    public int min(int x, int y, int z){
        if (x<y){
            if (z<x) return z;
            else return x;
        }else{
            if (z<y) return z;
            else return y;
        }
    }
}