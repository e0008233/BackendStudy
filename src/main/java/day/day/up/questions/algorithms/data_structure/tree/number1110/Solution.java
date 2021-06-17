package day.day.up.questions.algorithms.data_structure.tree.number1110;

import day.day.up.questions.algorithms.number102.TreeNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    List<TreeNode> ans;
    Map<Integer,Integer> map;
    List<TreeNode> toRemoveList;
    public List<TreeNode> delNodes(TreeNode root, int[] to_delete) {
        ans = new ArrayList<>();
        map = new HashMap<>();
        toRemoveList = new ArrayList<>();
        if (to_delete.length==0 || root==null){
            ans.add(root);
            return ans;
        }

        for (int num:to_delete){
            map.put(num,1);
        }
        root = deleteNode(root, to_delete, false);

        return ans;
    }

    private TreeNode deleteNode(TreeNode root,int[] to_delete, boolean isAdded) {
        if (root==null) return null;

        root.left= deleteNode(root.left,to_delete,false);
        root.right = deleteNode(root.right,to_delete,false);

        boolean toRemove = map.containsKey(root.val);
        if (!toRemove&&!isAdded) ans.add(root);
        if (toRemove){
            root.left= deleteNode(root.left,to_delete,false);
            root.right = deleteNode(root.right,to_delete,false);
            return null;
        }
        else{
            root.left= deleteNode(root.left,to_delete,true);
            root.right = deleteNode(root.right,to_delete,true);
            return root;
        }
    }
}