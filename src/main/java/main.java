import questions.algorithms.dp.number221.*;

import java.util.ArrayList;
import java.util.List;

// Interesting question: number32(parentheses), number 84
public class main {
    public static void main(String[] args){
        Solution solution = new Solution();

        System.out.println(solution.maximalSquare(new char[][]{{'1','0','1','0','0'},{'1','0','1','1','1'},{'1','1','1','1','1'},{'1','0','0','1','0'}}));
    }
}