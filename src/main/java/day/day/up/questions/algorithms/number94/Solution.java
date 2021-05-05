package day.day.up.questions.algorithms.number94;

import java.util.ArrayList;
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
