import questions.algorithms.dp.number120.*;

import java.util.ArrayList;
import java.util.List;

// Interesting question: number32, number 84
public class main {
    public static void main(String[] args){
        Solution solution = new Solution();
//        int[] input = new int[]{2,1,5,6,2,3};
        List<List<Integer>> triangle = new ArrayList<>();
        List<Integer> list1 = new ArrayList<>();
        List<Integer> list2= new ArrayList<>();
        List<Integer> list3 = new ArrayList<>();
        List<Integer> list4 = new ArrayList<>();

        list1.add(-1);
        list2.add(2);
        list2.add(3);
        list3.add(1);
        list3.add(-1);
        list3.add(-3);
//        list4.add(4);
//        list4.add(1);
//        list4.add(8);
//        list4.add(3);


        triangle.add(list1);
        triangle.add(list2);
        triangle.add(list3);
//        triangle.add(list4);


        System.out.println(solution.minimumTotal(triangle));
    }
}
