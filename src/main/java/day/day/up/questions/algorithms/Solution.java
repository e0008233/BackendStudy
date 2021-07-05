package day.day.up.questions.algorithms;
//Q:给定1个正整数数组array和1个正整数n，从array中寻找和值大于等于n的最短子数组（sum>=target）。
//        如果存在，则返回最短子数组长度；如果不存在返回0。
//        例如：array = 1, 3, 4, 3, 9, 1, n = 12, 那么子数组3, 9满足条件且长度最短为2
//        子数组的概念是，顺序跟原数组一致，且连续
//Q:给定1个正整数数组array和1个正整数n，从array中寻找和值大于等于n的最短子数组（sum>=target）。
//        如果存在，则返回最短子数组长度；如果不存在返回0。
//        例如：array = 1, 3, 4, 3, 9, 1, n = 12, 那么子数组3, 9满足条件且长度最短为2
//        子数组的概念是，顺序跟原数组一致，且连续
//        Q:求一个树的最长路径的长度，路径的定义是：任意两个节点连在一起经过的节点数（包括两端）
//        Q:两个单调递增的数据，长度分别为M和N，求这两个数组中所有数值的中位数（第(M+N)/2小的数）
//        PS：不允许合并
public class Solution {

    public static int findShortestLength(int [] number, int target){
        if (number.length==0) return 0;


        int start = 0;
        int newStart= 0;
        int currentSum = number[0];
        int minLength = Integer.MAX_VALUE;

        if (currentSum>=target) return 1;


        for (int i=1;i<number.length;i++){
            currentSum = currentSum + number[i];
            if (currentSum>=target){
                // new start window
                newStart = getShortestStart(number,target,start,i,currentSum);

                int length = i-newStart+1;
                if (length<minLength) minLength= length;


                // modify the sum
                for (int j =start;j<newStart;j++){
                    currentSum= currentSum - number[j];
                }
                start = newStart;
            }


        }
        if (minLength==Integer.MAX_VALUE) return 0;

        return minLength;
    }

    private static int getShortestStart(int[] number, int target, int start,int end,int currentSum) {
        int i = start;
        for (i = start;i<=end;i++){
            currentSum= currentSum-number[i];
            if (currentSum<target){
                break;
            }
        }

        return i;
    }
}
