package day.day.up.questions.algorithms.divide_and_conquer.number726;

import java.util.*;

// https://blog.csdn.net/katrina95/article/details/99947645


public class Solution {
    public String countOfAtoms(String formula) {
        StringBuilder result = new StringBuilder();
        Map<String, Integer> map = helper(formula);
        List<String> res = new ArrayList<>();
        for (String str : map.keySet()) {
            res.add(str + (map.get(str) > 1 ? map.get(str) : ""));
        }
        Collections.sort(res);
        for (String str : res) {
            result.append(str);
        }
        return result.toString();
    }

    public Map<String, Integer> helper(String s) {
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '(') {
                int index = read(s, i + 1);
                Map<String, Integer> innerMap = helper(s.substring(i + 1, index));
                int mult = 1;
                i = index - 1;

                if (index+1<s.length()&&Character.isDigit(s.charAt(index + 1))) {
                    int numIndex = readNum(s, index + 1);
                    mult = Integer.valueOf(s.substring(index + 1, numIndex));
                    i = numIndex - 1;
                }
                for (String str : innerMap.keySet()) {
                    if (map.containsKey(str)) map.put(str, map.get(str) + innerMap.get(str) * mult);
                    else map.put(str, innerMap.get(str) * mult);
                }
            } else if (Character.isLetter(c)) {
                int eleIndex = readElement(s, i + 1);
                String ele = s.substring(i, eleIndex);
                int count = 1;
                i = eleIndex - 1;
                if (eleIndex < s.length() && Character.isDigit(s.charAt(eleIndex))) {
                    int numIndex = readNum(s, eleIndex);
                    count = Integer.valueOf(s.substring(eleIndex, numIndex));
                    i = numIndex - 1;
                }
                if (map.containsKey(ele)) map.put(ele, map.get(ele) + count);
                else map.put(ele, count);
            } else {
            }
        }
        return map;
    }

    public int read(String s, int i) {
        int count = 0;
        while (i < s.length()) {
            if (s.charAt(i) == '(') count++;
            else if (s.charAt(i) == ')') {
                if (count == 0) return i;
                else count--;
            }
            i++;
        }
        return -1;
    }

    public int readNum(String s, int i) {
        while (i < s.length() && Character.isDigit(s.charAt(i))) i++;
        return i;
    }

    public int readElement(String s, int i) {
        while (i < s.length() && Character.isLowerCase(s.charAt(i))) i++;
        return i;
    }
}