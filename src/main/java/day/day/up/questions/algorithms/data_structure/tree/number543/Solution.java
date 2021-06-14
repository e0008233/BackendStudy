package day.day.up.questions.algorithms.data_structure.tree.number543;

import day.day.up.questions.algorithms.number102.TreeNode;

public class Solution {
    int max;
    public int diameterOfBinaryTree(TreeNode root) {
        if (root ==null) return 0;
        max = 0;
        int left = findSize(root.left);
        int right = findSize(root.right);
        int result = 0;
        if (left>0) result=result+1+left;
        if (right>0) result = result+1+right;
        if (right>max) max = right;
        return max;
    }

    private int findSize(TreeNode curr) {
        if (curr ==null) return 0;

        int left = findSize(curr.left);
        int right = findSize(curr.right);
        int result = 0;
        if (left>0) result=result+1+left;
        if (right>0) result = result+1+right;
        if (right>max) max = result;

        return 1+Math.max(left,right);
    }


}