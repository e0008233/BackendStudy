package day.day.up.questions.algorithms.no_topics.number114;


// set a range for each node when doing recursion
// another method: inorder Traversal gives an increasing result

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Solution {
    List<List<Integer>> ans = new ArrayList<>();

    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {

        if (root==null) return ans;

        int direction = 0;
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
            direction++;

            if (direction%2 == 0){
                // for (TreeNode treeNode: allNodesAtThisLevel){
                //     if (treeNode.left!=null) queue.add(treeNode.left);
                //     if (treeNode.right!=null) queue.add(treeNode.right);
                // }
                for (int i=allNodesAtThisLevel.size()-1;i>=0;i--){
                    if (allNodesAtThisLevel.get(i).left!=null) queue.add(allNodesAtThisLevel.get(i).left);
                    if (allNodesAtThisLevel.get(i).right!=null) queue.add(allNodesAtThisLevel.get(i).right);

                }
            }
            else{

                for (int i=allNodesAtThisLevel.size()-1;i>=0;i--){
                    if (allNodesAtThisLevel.get(i).right!=null) queue.add(allNodesAtThisLevel.get(i).right);
                    if (allNodesAtThisLevel.get(i).left!=null) queue.add(allNodesAtThisLevel.get(i).left);

                }

//                 for (TreeNode treeNode: allNodesAtThisLevel){
//                     if (treeNode.right!=null) queue.add(treeNode.right);
//                     if (treeNode.left!=null) queue.add(treeNode.left);
//                 }
            }
            ans.add(toAdd);
        }

        return ans;
    }

}