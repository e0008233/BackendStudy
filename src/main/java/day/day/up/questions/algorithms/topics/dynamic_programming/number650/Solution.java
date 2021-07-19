package day.day.up.questions.algorithms.topics.dynamic_programming.number650;
//https://www.cnblogs.com/grandyang/p/7439616.html
//通过分析上面这6个简单的例子，已经可以总结出一些规律了，首先对于任意一个n(除了1以外)，
// 最差的情况就是用n步，不会再多于n步，但是有可能是会小于n步的，比如 n=6 时，就只用了5步，
// 仔细分析一下，发现时先拼成了 AAA，再复制粘贴成了 AAAAAA。
// 那么什么情况下可以利用这种方法来减少步骤呢，分析发现，小模块的长度必须要能整除n，这样才能拆分。
// 对于 n=6，我们其实还可先拼出 AA，然后再复制一次，粘贴两次，得到的还是5。分析到这里，
// 解题的思路应该比较清晰了，找出n的所有因子，然后这个因子可以当作模块的个数，再算出模块的长度 n/i，
// 调用递归，加上模块的个数i来更新结果 res 即可，
public class Solution {
    public int minSteps(int n) {
        if (n==1) return 0;
        int[] result = new int[n+1];
        for (int i= 1; i<=n;i++){
            result[i] = i;   // worst case: copy the first letter and paste n-1 times;
            int limit = (int)Math.sqrt(i);
            for (int j=i-1;j>=2;j--){
                if (i%j ==0){
                    result[i] = Math.min(result[i],result[j]+i/j);
                    break;
                }
            }
        }

        return result[n];

    }
}