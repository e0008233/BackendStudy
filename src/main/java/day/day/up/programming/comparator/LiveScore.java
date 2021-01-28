package day.day.up.programming.comparator;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class LiveScore implements Serializable {
    private static final long serialVersionUID = -6894762781323259600L;
    private int guvScore;
    private int diamondScore;
    private int trendingScore;

    public String toString() {
        return "\n-------------------\n" +
                "guvScore=" + this.guvScore +
                "\ndiamondScore=" + this.diamondScore +
                "\ntrendingScore=" + this.trendingScore;
    }
}
