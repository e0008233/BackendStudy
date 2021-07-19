package day.day.up.questions.algorithms.topics.search_bfs_dfs_backtrack.number127;

import java.util.*;

public class Solution {
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {

        Set<String> words = new HashSet<>(wordList);
        if(!words.contains(endWord))
            return 0;

        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        beginSet.add(beginWord);
        endSet.add(endWord);

        //bidirectional BFS method.

        return biBFS(beginSet, endSet, words, false, 0);
    }

    private int biBFS(Set<String> beginSet, Set<String> endSet, Set<String> words, boolean reverse, int count){

        if(beginSet.isEmpty() || endSet.isEmpty()) {
            return 0;
        }

        if (beginSet.size() > endSet.size()) {
            return biBFS(endSet, beginSet, words, !reverse,count);
        }

        boolean found = false;
        words.removeAll(beginSet);
        Set<String> nextSequence = new HashSet<>();

        //BFS from beginSet.
        for(String oldWord : beginSet){
            char[] charSet = oldWord.toCharArray();
            for(int i=0; i< charSet.length; i++){
                char c = charSet[i];
                for(char j='a'; j<= 'z'; j++){
                    charSet[i] = j;
                    String newWord = new String(charSet);

                    if(words.contains(newWord)){
                        if(endSet.contains(newWord)){
                            return count+1;
                        }else{
                            nextSequence.add(newWord);
                        }
                        // build the map in the direction of shorter set to longer set depending on the size of the set.

                    }
                }
                charSet[i] = c;
            }
        }
        if(!nextSequence.isEmpty()){
            return biBFS(nextSequence,endSet, words, reverse,count+1);
        }

        return 0;
    }

}
