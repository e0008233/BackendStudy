package programming.comparator;


import java.io.Serializable;

public class LiveScore implements Serializable {
    private static final long serialVersionUID = -6894762781323259600L;
    private double hotScore;
    private double trendingScore;
    private int guvScore;
    private int diamondScore;


    public double getHotScore() {
        return this.hotScore;
    }

    public double getTrendingScore() {
        return this.trendingScore;
    }

    public int getGuvScore() {
        return this.guvScore;
    }

    public int getDiamondScore() {
        return this.diamondScore;
    }

    public void setHotScore(double hotScore) {
        this.hotScore = hotScore;
    }

    public void setTrendingScore(double trendingScore) {
        this.trendingScore = trendingScore;
    }

    public void setGuvScore(int guvScore) {
        this.guvScore = guvScore;
    }

    public void setDiamondScore(int diamondScore) {
        this.diamondScore = diamondScore;
    }

}
