package day.day.up.questions.algorithms.search_bfs_dfs_backtrack.number126;

import java.util.*;

class Solution3 {
    class Node{
        String value;
        List<Solution.Node> next;
        int length;

        public Node(){}
        public Node(String value){
            this.value=value;
        }

        @Override
        public boolean equals(Object obj){
            Solution.Node node =  (Solution.Node)obj;
            return this.value.equals(node.value);
        }

    }

    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> result = new ArrayList<List<String>>();

        Set<String> words = new HashSet<>(wordList);
        if(!words.contains(endWord))
            return result;

        Set<String> beginSet = new HashSet<>();
        Set<String> endSet = new HashSet<>();
        beginSet.add(beginWord);
        endSet.add(endWord);

        //HashSet to store all possible transformations.
        HashMap<String, List<String>> map = new HashMap<>();
        //bidirectional BFS method.
        biBFS(beginSet, endSet, words, map, false);

        //if(!hasSequence) return result;

        List<String> temp = new ArrayList<String>(Arrays.asList(beginWord));
        DFS(beginWord, endWord, map, result, temp);

        return result;
    }

    private void  biBFS(Set<String> beginSet, Set<String> endSet, Set<String> words, HashMap<String, List<String>> map, boolean reverse){

        if (beginSet.size() > endSet.size()) {
            biBFS(endSet, beginSet, words, map, !reverse);
            return;
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
                            found = true;
                        }else{
                            nextSequence.add(newWord);
                        }
                        // build the map in the direction of shorter set to longer set depending on the size of the set.

                        String key = reverse ? newWord : oldWord;
                        String val = reverse ? oldWord : newWord;

                        if (!map.containsKey(key)) {
                            map.put(key, new ArrayList());
                        }
                        map.get(key).add(val);
                    }
                }
                charSet[i] = c;
            }
        }
        if(!found && !nextSequence.isEmpty()){
            biBFS(nextSequence,endSet, words, map, reverse);
        }
    }

    private void DFS(String beginWord, String endWord, HashMap<String, List<String>> map, List<List<String>> result, List<String> temp){
        if(beginWord.equals(endWord)) { //boundary case.
            result.add(new ArrayList<String>(temp));
        }

        if(!map.containsKey(beginWord)) return; //if the beginWord is not in map, then it is not the shortest path to the endWord.

        for(String s : map.get(beginWord)){
            temp.add(s);
            DFS(s, endWord, map, result, temp);
            temp.remove(temp.size()-1); //remove the added word.
        }
    }
}
