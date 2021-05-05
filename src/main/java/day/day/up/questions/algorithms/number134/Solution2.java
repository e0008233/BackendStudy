package day.day.up.questions.algorithms.number134;


// If you carefully observe at the end if we check total gas - cost is >= 0 which means we have a solution and
// as per problem note we must be have 1 solution.
// Now the point how to find from which station we have to start?
//
// If we Start from 0th stop and check fuel tank at every stop whenver it goes -ve which implies that
// we can't start from all the previous station and including i`th station -> we will update our start station from i + 1 and reset tank to 0.
public class Solution2 {

    public int canCompleteCircuit(int[] gas, int[] cost) {

        int start = -1;
        int remaining = 0;

        int total = 0; // for whether it is possible to complete the trip;
        for (int i = 0; i < gas.length; i++) {
            total = total +gas[i]-cost[i];
            if (start==-1){
                if (remaining+gas[i]>=cost[i]){
                    start = i;
                    remaining = remaining+gas[i]-cost[i];
                }
            }
            else{
                if (remaining+gas[i]>=cost[i]){
                    remaining = remaining+gas[i]-cost[i];
                }
                else {
                    start = -1;
                    remaining = 0;
                }
            }
        }
        if (total<0) return -1;
        return start;
    }

}