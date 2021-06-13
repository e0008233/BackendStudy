package day.day.up.questions.algorithms.data_structure.tree.leetcode105;

import day.day.up.questions.algorithms.number102.TreeNode;

import java.util.HashMap;
import java.util.Map;


public class Solution {

    Map<Integer, Integer> map;
    public TreeNode buildTree(int[] preorder, int[] inorder) {
        if (preorder.length<=0) return null;
        map = new HashMap<>();

        for (int i=0;i<inorder.length;i++){
            map.put(inorder[i],i);
        }
        int rootIndex = map.get(preorder[0]);
        TreeNode root = new TreeNode(preorder[0]);
        root.left = helper(preorder,inorder,1,rootIndex,0,rootIndex-1);
        root.right = helper(preorder,inorder,rootIndex+1,preorder.length-1,rootIndex+1,preorder.length-1);
        return root;
    }

    private TreeNode helper(int[] preorder, int[] inorder, int preorderStart, int preorderEnd, int inorderStart, int inorderEnd) {
        if (preorderEnd<preorderStart) return null;
        if (preorderEnd==preorderStart) return new TreeNode(preorder[preorderStart]);

        int rootIndex = map.get(preorder[preorderStart]);

        TreeNode root = new TreeNode(preorder[preorderStart]);

        root.left = helper(preorder,inorder,preorderStart+1,preorderStart+1+(rootIndex-1-inorderStart),inorderStart,rootIndex-1);

        root.right = helper(preorder,inorder,preorderEnd-(inorderEnd-(rootIndex+1)),preorderEnd,rootIndex+1,inorderEnd);

        return root;

    }
}