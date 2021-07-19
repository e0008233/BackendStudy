package day.day.up.questions.algorithms.no_topics.number28;

public class Solution {
    public int strStr(String haystack, String needle) {
        if (needle==null || needle.isEmpty()) return 0;
        if (haystack==null || haystack.isEmpty()) return -1;

        for (int i=0;i<haystack.length();i++){
            if (checkEquals(haystack,needle,i)) return i;
        }

        return -1;
    }

    private boolean checkEquals(String haystack, String needle, int start) {
        for (int i=0;i<needle.length();i++) {
            if (start+i>=haystack.length()) return false;
            if (needle.charAt(i)!=haystack.charAt(start+i)) return false;
        }
        return true;
    }
}
