package questions.algorithms.dp.number70;

// find the max ending at each array position
// time complexity O(n)
class Solution {
    public int climbStairs(int n) {
        if (n==1||n==0) return 1;
        if (n==2) return 2;

        int ans[]=new int[n+1];
        ans[0]=1;
        ans[1]=1;
        ans[2]=2;
        for (int i=3;i<=n;i++){
            ans[i]=ans[i-1]+ans[i-2];
        }
        return ans[n];
    }
}