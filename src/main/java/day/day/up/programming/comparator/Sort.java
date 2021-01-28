package day.day.up.programming.comparator;

import java.util.*;

// sorting based on multiple properties
public class Sort {

    public void sort(List<LiveScore> list){


        list.get(0).getGuvScore();
        list.sort(Comparator.comparing(e->((LiveScore)e).getGuvScore()).reversed()
                .thenComparing(e->((LiveScore)e).getDiamondScore()).reversed().reversed()
                .thenComparing(e->((LiveScore)e).getTrendingScore()));

        System.out.println(list);
    }


    public void sort(){
        List<LiveScore> list = new ArrayList<>();
        list.add(new LiveScore(5,5,5));
        list.add(new LiveScore(5,4,5));
        list.add(new LiveScore(6,1,1));

        list.add(new LiveScore(4,7,5));
        list.add(new LiveScore(4,7,3));


        //method 1:
        Comparator<LiveScore> comparator = Comparator
                .comparing(LiveScore::getGuvScore,Comparator.reverseOrder())
                .thenComparing(LiveScore::getDiamondScore,Comparator.reverseOrder())
                .thenComparing(LiveScore::getTrendingScore,Comparator.naturalOrder());
        Collections.sort(list,comparator);

        //method 2:
        list.sort(Comparator.comparing(e->((LiveScore)e).getGuvScore()).reversed()
                .thenComparing(Comparator.comparing(e->((LiveScore)e).getDiamondScore()).reversed())
                .thenComparing(e->((LiveScore)e).getTrendingScore()));

        System.out.println(list);
    }
}
