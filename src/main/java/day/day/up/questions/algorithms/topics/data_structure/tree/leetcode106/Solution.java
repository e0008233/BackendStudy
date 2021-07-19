package day.day.up.questions.algorithms.topics.data_structure.tree.leetcode106;

import day.day.up.questions.algorithms.no_topics.number102.TreeNode;

import java.util.HashMap;
import java.util.Map;


public class Solution {

    Map<Integer, Integer> map;

    public TreeNode buildTree(int[] inorder, int[] postorder) {

        if (postorder.length<=0) return null;
        map = new HashMap<>();

        for (int i=0;i<inorder.length;i++){
            map.put(inorder[i],i);
        }

        int rootIndex = map.get(postorder[postorder.length-1]);
        TreeNode root = new TreeNode(postorder[postorder.length-1]);

        root.left = helper(postorder,inorder,0,rootIndex-1,0,rootIndex-1);
        root.right = helper(postorder,inorder,rootIndex,postorder.length-2,rootIndex+1,inorder.length-1);
        return root;

    }


    private TreeNode helper(int[] postorder, int[] inorder, int postorderStart, int postorderEnd, int inorderStart, int inorderEnd) {
        if (postorderEnd<postorderStart) return null;
        if (postorderEnd==postorderStart) return new TreeNode(postorder[postorderStart]);

        int rootIndex = map.get(postorder[postorderEnd]);

        TreeNode root = new TreeNode(postorder[postorderEnd]);

        root.left = helper(postorder,inorder,postorderStart,postorderStart+(rootIndex-1-inorderStart),inorderStart,rootIndex-1);

        root.right = helper(postorder,inorder,postorderEnd-1-(inorderEnd-(rootIndex+1)),postorderEnd-1,rootIndex+1,inorderEnd);

        return root;

    }
}