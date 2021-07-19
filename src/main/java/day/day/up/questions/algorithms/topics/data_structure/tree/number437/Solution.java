package day.day.up.questions.algorithms.topics.data_structure.tree.number437;

import day.day.up.questions.algorithms.no_topics.number102.TreeNode;


//递归每个节点时，需要分情况考虑:(1)如果选取该节点加入路径，则之后必须继续加入连 续节点，或停止加入节点
// (2)如果不选取该节点加入路径，则对其左右节点进行重新进行考虑。
// 因此一个方便的方法是我们创建一个辅函数，专门用来计算连续加入节点的路径。
public class Solution {
    public int pathSum(TreeNode root, int targetSum) {
      if (root==null) return 0;
      return pathSumWithRoot(root,targetSum)+    //with root
              pathSum(root.left,targetSum)+pathSum(root.right,targetSum);  //without root
    }

    //(1)如果选取该节点加入路径，则之后必须继续加入连 续节点，或停止加入节点
    private int pathSumWithRoot(TreeNode root, int targetSum) {
        if (root==null) return 0;
        int count = 0;
        if (root.val==targetSum) count++;
        return count+pathSumWithRoot(root.left,targetSum-root.val)+pathSumWithRoot(root.right,targetSum-root.val);
    }
}