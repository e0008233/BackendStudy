package day.day.up.questions.algorithms.math.number504;

import java.util.Arrays;


//从 1 到 n 遍历，假设当前遍历到 m，则把所有小于 n 的、且是 m 的倍 数的整数标为和数;遍历完成后，没有被标为和数的数字即为质数。
public class Solution {
    public String convertToBase7(int num) {
        if (num==0) return "0";
        boolean isNegative = false;
        if (num < 0){
            num = 0 - num;
            isNegative = true;
        }
        String result = "";
        while (num != 0) {
            int remainder = num % 7;
            num= num/7;

            result = remainder + result;
        }
        if (isNegative){
            result="-"+result;
        }

        return result;
    }


}