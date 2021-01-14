package questions.algorithms.number95;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


// For node n, left tree node can be (1..n-1), right tree can be (n+1..limit)
// Use recursion to return the tree node
public class Solution {
    public static List<TreeNode> generateTrees(int n) {
        if (n == 0) return new ArrayList<TreeNode>();
        HashMap<Pair<Integer,Integer>,List<TreeNode>> results = new HashMap<Pair<Integer, Integer>, List<TreeNode>>();
        return helper(1,n,results);
    }

    public static List<TreeNode> helper(int start, int end, HashMap<Pair<Integer, Integer>, List<TreeNode>> results){
        List<TreeNode> result = new ArrayList<TreeNode>();
        if (start>end) {
            result.add(null);
            return result;
        }
        Pair<Integer,Integer> pair = new Pair<Integer, Integer>(start, end);
        if (results.containsKey(pair)) return results.get(pair);
        for (int i=start;i<=end;i++){
            List<TreeNode> left = helper(start,i-1,results);
            List<TreeNode> right = helper(i+1,end,results);
            for (TreeNode leftNode: left){
                for (TreeNode rightNode:right){
                    TreeNode root = new TreeNode(i);
                    root.left=leftNode;
                    root.right=rightNode;
                    result.add(root);
                }
            }
        }
        results.put(pair,result);
        return result;
    }

    private static class Pair<I extends Number, I1 extends Number> {
        public final int start;
        public final int end;

        public Pair(Integer start, Integer end) {
            this.start = start;
            this.end = end;;
        }
        @Override
        public boolean equals(Object obj){
            if (((Pair) obj).start==this.start && ((Pair) obj).end==this.end) return true;
            else return false;
        }

    }

}
