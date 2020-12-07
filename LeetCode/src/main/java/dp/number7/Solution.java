package dp.number7;

public class Solution {
    public static int reverse(int x) {
        int ans = 0;
        if (x==0|| x==Integer.MIN_VALUE) return ans;

        boolean isPositive = true;
        if (x<0) {
            isPositive = false;
            x = x*-1;
        }
        while (x!=0){
            int temp = x % 10;
            x=x /10;
            if (isPositive && ans > Integer.MAX_VALUE/10 || (ans == Integer.MAX_VALUE / 10 && temp > 7)) return 0;
            if ((!isPositive) && ans > Integer.MAX_VALUE/10 || (ans == Integer.MAX_VALUE / 10 && temp > 8)) return 0;
            ans=ans*10+temp;
        }
        if (!isPositive) ans = ans *-1;
        return ans;
    }
}
