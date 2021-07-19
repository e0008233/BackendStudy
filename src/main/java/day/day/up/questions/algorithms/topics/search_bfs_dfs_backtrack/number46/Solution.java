package day.day.up.questions.algorithms.topics.search_bfs_dfs_backtrack.number46;

import java.util.ArrayList;
import java.util.List;

//对于每一个当前位置 i，我们可以将其于之后的任意位置交换， 然后继续处理位置 i+1，直到处理到最后一位。
// 为了防止我们每此遍历时都要新建一个子数组储 存位置 i 之前已经交换好的数字，我们可以利用回溯法，只对原数组进行修改，在递归完成后再 修改回来。
class Solution {
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();

        backTracking(nums,0,ans);
        return ans;
    }

    private void backTracking(int[] nums, int i, List<List<Integer>> ans) {
        if (i==nums.length){
            List<Integer> toAdd = new ArrayList<>();
            for (int k:nums){
                toAdd.add(k);
            }
            ans.add(toAdd);
        }

        for (int j=i;j<nums.length;j++){
            swap(nums,i,j);
            backTracking(nums,i+1,ans);
            swap(nums,i,j);   // swap back the elements, backtracking
        }
    }

    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

}