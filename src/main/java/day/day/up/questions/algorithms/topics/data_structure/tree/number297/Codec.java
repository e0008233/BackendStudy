package day.day.up.questions.algorithms.topics.data_structure.tree.number297;

import day.day.up.questions.algorithms.no_topics.number102.TreeNode;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode(int x) { val = x; }
 * }
 */
public class Codec {

    // Encodes a tree to a single string.
    public String serialize(TreeNode root) {
        String ans = preOrder(root);
        System.out.println(ans);

        return ans;

    }

    private String preOrder(TreeNode root) {

        if (root==null){
            return "*,";
        }
        String ans ="";
        ans=ans+root.val+",";
        ans = ans + preOrder(root.left);
        ans= ans +preOrder(root.right);
        return ans;

    }

    // Decodes your encoded data to tree.
    public TreeNode deserialize(String data) {
        String[] input = data.split(",");
        Queue<String> queue = new LinkedList<>();
        for (String string: input){
            queue.add(string);
        }
        TreeNode node = helper(queue);

        return node;

    }

    private TreeNode helper(Queue<String> queue) {
        if (queue.isEmpty()) return null;
        String curr = queue.remove();

        if (!curr.equals("*")){
            TreeNode root = new TreeNode(Integer.valueOf(curr));
            root.left = helper(queue);
            root.right= helper(queue);
            return root;
        }

        return null;

    }
}

// Your Codec object will be instantiated and called as such:
// Codec ser = new Codec();
// Codec deser = new Codec();
// TreeNode ans = deser.deserialize(ser.serialize(root));