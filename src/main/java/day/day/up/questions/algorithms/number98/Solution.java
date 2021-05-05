package day.day.up.questions.algorithms.number98;


// set a range for each node when doing recursion
// another method: inorder Traversal gives an increasing result

class Solution {
    public boolean isValidBST(TreeNode root) {
        if (root==null) return true;
        return helper(root,null,null);
    }

    public boolean helper(TreeNode root, Integer low, Integer high){
        if (root==null) return true;

        if (low!=null&&root.val<=low) {
            return false;
        }

        if (high!=null&&root.val>=high) {
            return false;
        }

        if (helper(root.left,low,root.val) && helper(root.right,root.val,high)) return true;

        return false;
    }
}