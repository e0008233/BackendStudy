package day.day.up.questions.algorithms.search_bfs_dfs_backtrack.number126;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Solution {
    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> ans = new ArrayList<>();

        if (!wordList.contains(endWord)) return ans;

        wordList.remove(beginWord);
        wordList.remove(endWord);

        Queue<String> shorterQueue = new LinkedList<>();
        shorterQueue.add(beginWord);

        Queue<String> theOtherQueue = new LinkedList<>();
        theOtherQueue.add(endWord);

        boolean isFound =false;
        boolean backward = false;

        // bi-directional bfs
        while (!shorterQueue.isEmpty() && !theOtherQueue.isEmpty() && !isFound) {
            if (shorterQueue.size()>theOtherQueue.size()){
                swap(shorterQueue,theOtherQueue);
                backward = !backward;
            }
            char [] arr = shorterQueue.remove().toCharArray();
            for (int i = 0; i < beginWord.length(); i++) {
                char ori = arr[i];
                for(char c = 'a'; c <= 'z'; c++) {
                    if(ori == c) continue;
                }
            }
        }
        return ans;
    }

    private void swap(Queue<String> shorterQueue, Queue<String> theOtherQueue) {
        Queue<String> temp = shorterQueue;
        shorterQueue = theOtherQueue;
        theOtherQueue = temp;
    }
}