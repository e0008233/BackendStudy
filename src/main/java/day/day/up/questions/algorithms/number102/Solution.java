package day.day.up.questions.algorithms.number102;


// set a range for each node when doing recursion
// another method: inorder Traversal gives an increasing result

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution {
    List<List<Integer>> ans = new ArrayList<>();

    public List<List<Integer>> levelOrder(TreeNode root) {

        if (root==null) return ans;

        Queue<TreeNode> queue= new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()){
            List<Integer> toAdd = new ArrayList<>();
            List<TreeNode> allNodesAtThisLevel = new ArrayList<>();
            while(!queue.isEmpty()) {
                TreeNode node = queue.remove();
                toAdd.add(node.val);
                allNodesAtThisLevel.add(node);
            }

            for (TreeNode treeNode: allNodesAtThisLevel){
                if (treeNode.left!=null) queue.add(treeNode.left);
                if (treeNode.right!=null) queue.add(treeNode.right);
            }
            ans.add(toAdd);
        }

        return ans;
    }

}