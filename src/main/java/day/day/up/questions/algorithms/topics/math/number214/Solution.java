package day.day.up.questions.algorithms.topics.math.number214;

import java.util.Arrays;


//从 1 到 n 遍历，假设当前遍历到 m，则把所有小于 n 的、且是 m 的倍 数的整数标为和数;遍历完成后，没有被标为和数的数字即为质数。
public class Solution {
    public int countPrimes(int n) {
        if (n<=2) return 0;
        boolean[] prime = new boolean[n+1];
        Arrays.fill(prime,true);
        int count = n-2;   // remove 1 and n

        for (int i=2;i<n;i++){
            if (prime[i]){
                for (int j=2 * i; j<n;j=j+i){
                    if (prime[j]){
                        prime[j] = false;
                        count--;
                    }
                }
            }
        }
        return count;

    }
}