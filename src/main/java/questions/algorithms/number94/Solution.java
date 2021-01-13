package questions.algorithms.number94;

import questions.algorithms.number94.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Solution {
    List<Integer> result;
    public List<Integer> inorderTraversal(TreeNode root) {
        result= new ArrayList<>();

        helper(root);
        return result;
    }

    private void helper(TreeNode root) {
        if (root==null){
            return;
        }

        helper(root.left);
        result.add(root.val);
        helper(root.right);

    }

}
