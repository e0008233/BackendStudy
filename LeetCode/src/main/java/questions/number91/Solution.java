package questions.number91;

public  class Solution {
    public int numDecodings(String s) {
        if (s.length() == 0 || s.charAt(0) == '0') return 0;
        else if (s.length() == 1) return 1;

        // if (s.length() == 2 && Integer.valueOf(s) <= 26 && s.charAt(1) > '0') return 2;
        // else if (s.length() == 2) return 1;
        int[] result = new int[s.length()];
        if (s.charAt(1) > '0') result[0] = 1;
        else result[0] = 0;
        if (Integer.valueOf(s.substring(0, 2)) <= 26 && s.charAt(1) > '0' && s.charAt(0) > '0') result[1] = 2;
        else if (s.charAt(1) =='0' && (s.charAt(0)=='1' || s.charAt(0)=='2') ) result[1] = 1;
        else if (s.charAt(1) =='0') result[1] = 0;
        else result[1] = 1;
        if (s.length() == 2) return result[1];
        for (int i = 2; i < s.length(); i++) {
            if (s.charAt(i) == '0') {
                if (s.charAt(i - 1) != '1' && s.charAt(i - 1) != '2') return 0;
                else result[i] = result[i - 2];
            } else if (s.charAt(i - 1) == '0') {
                result[i] = result[i - 1];
            } else if (Integer.valueOf(s.substring(i - 1, i+1)) <= 26) {
                result[i] = result[i - 1] + result[i - 2];
            } else {
                result[i] = result[i - 1];
            }
        }
        return result[s.length() - 1];
    }

}