package day.day.up.questions.algorithms.sorting.number347;



import java.util.HashMap;

public class Solution {
    public static HashMap<Integer, Integer> counter = new HashMap<>();

    public int[] topKFrequent(int[] nums, int k) {
        int[] result = new int[k];
        counter = new HashMap<>();

        for (int i:nums){
            counter.merge(i,1,Integer::sum);
        }

        // array of unique elements
        int n = counter.size();
        int[] unique = new int[n];
        int i = 0;
        for (int num: counter.keySet()) {
            unique[i] = num;
            i++;
        }

        return heapSort(unique,k,result);
    }


    private int[] heapSort(int[] arr, int k, int[] result){
        int n = arr.length;

        // starting from the parent of most button right subtree
        for (int i=n/2-1;i>=0;i--){
            heapify(arr,i,n);
        }

        int index = 0;for (int i = n - 1; i >= n-k; i--) {
            // Move current root to end
            result[index] = arr[0];
            arr[0] = arr[i];
//            arr[i] = result[index];

            // call max heapify on the reduced heap
            heapify(arr, 0, i);
            index++;
        }


        return result;
    }

    private void heapify(int[] arr, int i, int n) {
        int largest = i;// Initialize largest as root
        int left = 2*i+1;
        int right = 2*i+2;


        // If left child is larger than root
        if (left < n && counter.get(arr[left]) > counter.get(arr[largest]))
            largest = left;

        // If right child is larger than largest so far
        if (right < n && counter.get(arr[right]) > counter.get(arr[largest]))
            largest = right;

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected sub-tree
            heapify(arr, largest,n);
        }
    }


}