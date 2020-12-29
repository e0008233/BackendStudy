package questions.algorithms.dp.number72;
//1. If last characters of two strings are same, nothing much to do. Ignore last characters and get count for remaining strings. So we recur for lengths m-1 and n-1.
//2. Else (If last characters are not same), we consider all operations on ‘str1’, consider all three operations on last character of first string, recursively compute minimum cost for all three operations and take minimum of three values.
//        Insert: Recur for m and n-1
//        Remove: Recur for m-1 and n
//        Replace: Recur for m-1 and n-1

public class Solution {
    public int minDistance(String word1, String word2) {
        return 0;
    }
}