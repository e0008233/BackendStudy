package day.day.up.questions.algorithms.no_topics.number295;

import java.util.PriorityQueue;

// https://blog.csdn.net/sky_s_limit/article/details/105310953
class MedianFinder {
    PriorityQueue<Integer> maxQueue;
    PriorityQueue<Integer> minQueue;
    int count;

    /** initialize your data structure here. */
    public MedianFinder() {
        count=0;
        maxQueue = new PriorityQueue<>((a,b)-> b-a);
        minQueue = new PriorityQueue<>((a,b)-> a-b);

    }

    public void addNum(int num) {
        count++;
        minQueue.add(num);
        maxQueue.add(minQueue.poll());
        if ((count&1) != 1){
            minQueue.add(maxQueue.poll());
        }
    }

    public double findMedian(){
        if ((count & 1) == 0) {
            return (maxQueue.peek() + minQueue.peek()) / 2.0;
        } else {
            return maxQueue.peek();
        }

    }
}

/**
 * Your MedianFinder object will be instantiated and called as such:
 * MedianFinder obj = new MedianFinder();
 * obj.addNum(num);
 * double param_2 = obj.findMedian();
 */