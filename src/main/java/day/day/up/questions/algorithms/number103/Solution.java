package day.day.up.questions.algorithms.number103;


// set a range for each node when doing recursion
// another method: inorder Traversal gives an increasing result

public class Solution {
    public void flatten(TreeNode root) {
        if (root==null) return;
        if(root.left==null && root.right==null) return;

        if (root.left!=null){
            TreeNode temp = root.right;

            root.right=root.left;
            root.left=null;
            TreeNode toAdd = root.right;

            while (toAdd.right!=null){
                toAdd=toAdd.right;
            }
            toAdd.right=temp;
        }

        flatten(root.right);
    }
}