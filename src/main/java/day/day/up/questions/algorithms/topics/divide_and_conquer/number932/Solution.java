package day.day.up.questions.algorithms.topics.divide_and_conquer.number932;

import java.util.ArrayList;
import java.util.List;

// https://leetcode.com/problems/beautiful-array/discuss/186679/C++JavaPython-Odd-+-Even-Pattern-O%28N%29

//
//First: divide and conquer, why to divide to odd and even part (or merge odd and even part together)?
//
//        that’s say, we have two part: odd = {1, 5, 3}, even = {2, 4, 6}
//
//        any number from odd part and any number from even part will alway obey the rule A[k] * 2 != A[i] + A[j]
//
//        Ex: 5 from odd part, 6 from even part, for any k between 5 and 6, A[k] * 2 != 5 + 6
//
//        So merge two parts will form beautiful array!


public class Solution {
    public int[] beautifulArray(int n) {
        List<Integer> temp = helper(n);
        int [] result = new int[n];
        int count = 0;
        for (int num:temp){
            result[count] = num;
            count ++;
        }
        return result;
    }

    private List<Integer> helper(int n) {
        List<Integer> result = new ArrayList<>();
        if (n==1){
            result.add(1);
            return result;
        }

        List<Integer> left = helper(n/2+n%2);  // all odd numbers
        List<Integer> right = helper(n/2);   // all even numbers

        List<Integer> combined = new ArrayList<>();

        for (int num: left){
            combined.add(num*2-1);
        }
        for (int num: right){
            combined.add(num*2);
        }

        return combined;
    }


}