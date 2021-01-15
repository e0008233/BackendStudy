package questions.algorithms.number103;


// set a range for each node when doing recursion
// another method: inorder Traversal gives an increasing result

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

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