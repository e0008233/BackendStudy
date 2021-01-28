package day.day.up.questions.algorithms.number134;

public class Solution {

    public int canCompleteCircuit(int[] gas, int[] cost) {

        int num = gas.length;

        for (int i=0; i<gas.length;i++){
            if (startHere(0,i,0,gas,cost)) return i;
        }

        return -1;
    }

    public boolean startHere(int count, int stop, int remaining,int[] gas, int[] cost){
        if (gas[stop]+remaining<cost[stop]) return false;
        if (count==gas.length) return true;
        count++;
        remaining = gas[stop]+remaining-cost[stop];

        stop = (stop+1)%gas.length;

        if (startHere(count,stop,remaining,gas,cost)){
            return true;
        }
        else{
            return false;
        }


    }
}