package day.day.up.questions.algorithms.data_structure.monotone_stack.number901;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class StockSpanner {
    private Stack<Integer> indexStack;
    private Stack<Integer> priceStack;

    private int index;
    public StockSpanner() {
        indexStack = new Stack<>();
        priceStack = new Stack<>();
        index=0;

    }

    public int next(int price) {


        int temp =-1;
        while(!indexStack.isEmpty()&&priceStack.peek()<=price){
            temp = indexStack.pop();
            priceStack.pop();
        }


        int result = 1;

        if (temp>-1){
            if(indexStack.isEmpty()){
                result = index+1;
            }
            else{
                result = index- indexStack.peek();
            }
        }
        indexStack.add(index);
        priceStack.add(price);
        index++;
        return result;
    }
}
