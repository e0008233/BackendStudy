package day.day.up.questions.algorithms.topics.data_structure.tree.number637;

import day.day.up.questions.algorithms.no_topics.number102.TreeNode;

import java.util.*;

class Solution {
    public List<Double> averageOfLevels(TreeNode root) {
        List<Double> ans = new ArrayList<>();
        Queue<TreeNode> level  = new LinkedList<>();

        if (root==null) return ans;
        level.add(root);

        while (!level.isEmpty()){
            int size = level.size();
            long sum = 0;
            long count = 0;
            for (int i=0;i<size;i++){
                TreeNode node = level.remove();
                sum = sum+ node.val;
                count=count+1;
                if (node.left!=null) level.add(node.left);
                if (node.right!=null) level.add(node.right);
            }
            if (count>0l) ans.add((double)(sum/count));
        }
        return ans;

    }
}