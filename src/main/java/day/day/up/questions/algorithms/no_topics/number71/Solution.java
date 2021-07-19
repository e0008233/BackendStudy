package day.day.up.questions.algorithms.no_topics.number71;


import java.util.HashMap;


//https://cloud.tencent.com/developer/article/1660069
public class Solution {
    public int leastInterval(char[] tasks, int n) {
        HashMap<Character,Integer> count = new HashMap<>();
        int maxCount = 0;
        int sameMax = 0;

        for (char curr: tasks){
            if (count.containsKey(curr)){
                count.put(curr,count.get(curr)+1);

            }
            else{
                count.put(curr,1);
            }

            if (count.get(curr)>maxCount) maxCount = count.get(curr);

        }
        for (Character key:count.keySet()){
            if (count.get(key)==maxCount) {
                sameMax ++;
            }
        }

        int total = (maxCount-1)*(n+1)+sameMax;


        return Math.max(tasks.length,total);
    }
}