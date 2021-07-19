package day.day.up.questions.algorithms.topics.two_pointer.number88;


public class Solution {
    public void merge(int[] nums1, int m, int[] nums2, int n) {
        int[] result = new int[m+n];
        int index1 = 0;
        int index2 = 0;

        int index= 0;
        while(index1<=m-1||index2<=n-1){
            if (index1>m-1){
                result[index]=nums2[index2];
                index2++;
            }
            else if (index2>n-1){
                result[index]=nums1[index1];
                index1++;
            }
            else if (nums1[index1]<=nums2[index2]){
                result[index]=nums1[index1];
                index1++;
            }
            else{
                result[index]=nums2[index2];
                index2++;
            }
            index++;
        }

        for (int i = 0; i<nums1.length;i++){
            nums1[i]=result[i];
        }
    }


}