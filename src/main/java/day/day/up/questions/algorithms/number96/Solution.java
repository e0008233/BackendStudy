package day.day.up.questions.algorithms.number96;

//Catalan number
//dp[2] =  dp[0] * dp[1]　　　(1为根的情况，则左子树一定不存在，右子树可以有一个数字)
//
//　　　　+ dp[1] * dp[0]　　  (2为根的情况，则左子树可以有一个数字，右子树一定不存在)
//
//同理可写出 n = 3 的计算方法：
//
//dp[3] =  dp[0] * dp[2]　　　(1为根的情况，则左子树一定不存在，右子树可以有两个数字)
//
//　　　　+ dp[1] * dp[1]　　  (2为根的情况，则左右子树都可以各有一个数字)
//
// 　　　  + dp[2] * dp[0]　　  (3为根的情况，则左子树可以有两个数字，右子树一定不存在


// For node n, left tree node can be (1..n-1), right tree can be (n+1..limit)
public class Solution {
    public int numTrees(int n) {
        if (n==0||n==1) return 1;
        int ans[] = new int[n+1];
        ans[0]=1;
        ans[1]=0;
        for (int i=2;i<=n;i++){
            for(int j=0; j<=i-1;j++){
                ans[i]=ans[i]+ans[j]*ans[i-1-j];
            }
        }
        return ans[n];
    }

}
