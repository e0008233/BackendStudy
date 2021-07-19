package day.day.up.questions.algorithms.topics.data_structure.tree.number101;

import day.day.up.questions.algorithms.no_topics.number102.TreeNode;


public class Solution {
    public boolean isSymmetric(TreeNode root) {
        if (root==null) return true;

        return isSymmetricHelper(root.left,root.right);
    }

    private boolean isSymmetricHelper(TreeNode left, TreeNode right) {
        if (right ==null){
            if (left==null) return true;
            return false;
        }

        if (left ==null){
            if (right==null) return true;
            return false;
        }

        if (left.val!=right.val) return false;

        return isSymmetricHelper(left.left,right.right) &&isSymmetricHelper(left.right,right.left);
    }
}