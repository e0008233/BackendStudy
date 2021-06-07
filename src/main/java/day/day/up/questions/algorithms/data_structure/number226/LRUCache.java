package day.day.up.questions.algorithms.data_structure.number226;

import day.day.up.questions.algorithms.number102.TreeNode;

import java.util.LinkedHashMap;
import java.util.Map;


/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    public TreeNode invertTree(TreeNode root) {
        invertTreeHelper(root);
        return root;
    }

    private void invertTreeHelper(TreeNode node) {
        if (node ==null) return;

        TreeNode temp = node.left;
        node.left=node.right;
        node.right = temp;
        invertTree(node.left);
        invertTree(node.right);
    }
}