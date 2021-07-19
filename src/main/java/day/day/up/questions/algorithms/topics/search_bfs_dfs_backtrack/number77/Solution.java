package day.day.up.questions.algorithms.topics.search_bfs_dfs_backtrack.number77;

import java.util.ArrayList;
import java.util.List;

public class Solution {
    public List<List<Integer>> combine(int n, int k) {
        List<List<Integer>> ans = new ArrayList<>();
        backtracking(n,k,ans,new ArrayList<Integer>(),1);
        return ans;
    }

    private void backtracking(int n, int k,List<List<Integer>> ans,List<Integer> list, int index) {
        if (list.size()==k){
            List<Integer> toAdd = new ArrayList<>(list);
            ans.add(toAdd);
            return;
        }

        for (int i = index;i<=n;i++){
            list.add(i);
            backtracking(n,k,ans,list,i+1);
            list.remove(list.size()-1);
        }
    }


}