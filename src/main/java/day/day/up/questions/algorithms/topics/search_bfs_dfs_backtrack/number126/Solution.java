package day.day.up.questions.algorithms.topics.search_bfs_dfs_backtrack.number126;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class Solution {
    class Node{
        String value;
        List<Node> next;
        int length;

        public Node(){}
        public Node(String value){
            this.value=value;
        }

        @Override
        public boolean equals(Object obj){
            Node node =  (Node)obj;
            return this.value.equals(node.value);
        }

    }

    public List<List<String>> findLadders(String beginWord, String endWord, List<String> wordList) {
        List<List<String>> ans = new ArrayList<>();

        if (!wordList.contains(endWord)) return new ArrayList<List<String>>();

        wordList.remove(beginWord);
        wordList.remove(endWord);

        Queue<Node> shorterQueue = new LinkedList<>();
        Node head = new Node(beginWord);
        shorterQueue.add(head);

        Queue<Node> theOtherQueue = new LinkedList<>();
        Node end = new Node(endWord);
        theOtherQueue.add(end);

        boolean isFound =false;
        boolean backward = false;

        // bi-directional bfs
        while (!shorterQueue.isEmpty() && !theOtherQueue.isEmpty() && !isFound) {
            if (shorterQueue.size()>theOtherQueue.size()){
                swap(shorterQueue,theOtherQueue);
                backward = !backward;
            }
            Node curr = shorterQueue.remove();

            List<Node> neighbours = new ArrayList<>();
            char [] arr = curr.value.toCharArray();

            for (int i = 0; i < beginWord.length(); i++) {
                char ori = arr[i];
                for(char c = 'a'; c <= 'z'; c++) {
                    if(ori == c) continue;

                    arr[i] = c;

                    String newWord = String.valueOf(arr);
                    Node newNode = new Node(newWord);

                    if (theOtherQueue.contains(newNode)){
                        isFound=true;

                    }
                    if (wordList.contains(newWord)){
                        // a neighbour is found
                    }
                }

                arr[i] = ori;
            }
        }
        return ans;
    }

    private void swap(Queue<Node> shorterQueue, Queue<Node> theOtherQueue) {
        Queue<Node> temp = shorterQueue;
        shorterQueue = theOtherQueue;
        theOtherQueue = temp;
    }
}