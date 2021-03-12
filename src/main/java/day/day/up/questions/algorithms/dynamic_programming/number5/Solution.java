package day.day.up.questions.algorithms.dynamic_programming.number5;


//Dynamic day.day.up.programming
//1. Maintain a boolean table[n][n] that is filled in bottom up manner.
//2. The value of table[i][j] is true, if the substring is palindrome, otherwise false.
//3. To calculate table[i][j], check the value of table[i+1][j-1], if the value is true and str[i] is same as str[j], then we make table[i][j] true.
//4. Otherwise, the value of table[i][j] is made false.
//5. We have to fill table previously for substring of length = 1 and length =2 because
//        as we are finding , if table[i+1][j-1] is true or false , so in case of
//        (i) length == 1 , lets say i=2 , j=2 and i+1,j-1 doesn’t lies between [i , j]
//        (ii) length == 2 ,lets say i=2 , j=3 and i+1,j-1 again doesn’t lies between [i , j].

public class Solution {
    public String longestPalindrome(String s) {
        if (s.isEmpty()) return "";
        if (s.length()==1){
            return s;
        }
        else if (s.length()==2){
             if (s.charAt(0)==s.charAt(1)) return s;
             else return s.substring(0,1);
        }

        int size = s.length();
        int maxSize = 1;
        int startingIndex = 0;
        boolean isPalindrome[][] = new boolean[size][size];
        for (int i=0;i<size;i++){
            isPalindrome[i][i]=true;
            if (i<size-1){
                if (s.charAt(i)==s.charAt(i+1)){
                    isPalindrome[i][i+1]=true;
                    maxSize=2;
                    startingIndex=i;
                }
            }
        }

        for (int j = 2; j<size;j++){
            for (int i=0;i<size-2;i++){
                if (j>i+1){
                    if (isPalindrome[i+1][j-1] && s.charAt(i)==s.charAt(j)){
                        isPalindrome[i][j]=true;
                        if (j-i+1>maxSize){
                            maxSize=j-i+1;
                            startingIndex=i;
                        }
                    }
                    else{
                        isPalindrome[i][j]=false;
                    }
                }
            }
        }

        return s.substring(startingIndex,startingIndex+maxSize);
    }
}
