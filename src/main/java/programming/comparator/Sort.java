package programming.comparator;

import java.util.Comparator;
import java.util.List;

public class Sort {

    public void sort(List<LiveScore> list){
        list.get(0).getGuvScore();
        list.sort(Comparator.comparing(e->((LiveScore)e).getGuvScore()).reversed()
                .thenComparing(e->((LiveScore)e).getDiamondScore()).reversed().reversed()
                .thenComparing(e->((LiveScore)e).getHotScore()));
    }
}
