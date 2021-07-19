package day.day.up.questions.algorithms.topics.data_structure.tree.number110;

import day.day.up.questions.algorithms.no_topics.number102.TreeNode;

//解法类似于求树的最大深度，但有两个不同的地方:一是我们需要先处理子树的深度再进行 比较，二是如果我们在处理子树时发现其已经不平衡了，则可以返回一个-1，
// 使得所有其长辈节 点可以避免多余的判断(本题的判断比较简单，做差后取绝对值即可;
// 但如果此处是一个开销较 大的比较过程，则避免重复判断可以节省大量的计算时间)。

public class Solution {
    public boolean isBalanced(TreeNode root) {
        if (root==null) return true;

        int leftSize = findSize(root.left);
        int rightSize = findSize(root.right);

        if (leftSize==-1 || rightSize==-1 || Math.abs(leftSize-rightSize)>1) return false;

        return true;
    }

    private int findSize(TreeNode curr) {
        if (curr==null) return 0;

        int leftSize = findSize(curr.left);
        int rightSize = findSize(curr.right);

        if (leftSize==-1 || rightSize==-1 || Math.abs(leftSize-rightSize)>1) return -1;

        return Math.max(leftSize,rightSize)+1;
    }
}