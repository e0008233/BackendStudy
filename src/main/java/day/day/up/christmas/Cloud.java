package day.day.up.christmas;

import java.util.Random;

import javax.swing.ImageIcon;

/**
 * A cloud is an animated object that creates snowflakes.
 * @author Barbara Lerner
 * @version Feb 10, 2010
 *
 */
public class Cloud extends Animation {

    // picture of a snowflake
    private ImageIcon snowPic;

    // size of the screen
    private int screenWidth, screenHeight;

    // used to generate random speeds and positions for snowflakes
    private Random snowGen;

    // The snowstorm that we add the flakes to.
    private SnowAnimation snowStorm;

    /**
     * Create a cloud that creates snowflakes.
     * @param animation the storm that the snow flakes are added to
     * @param aSnowPic the picture of the snowflake
     * @param aScreenWidth the width of the window
     * @param aScreenHeight the height of the window
     */
    public Cloud(SnowAnimation storm, ImageIcon aSnowPic, int aScreenWidth, int aScreenHeight) {
        super(900);

        // save the parameters for the "run" method
        this.snowStorm = storm;
        snowPic = aSnowPic;
        screenWidth = aScreenWidth;
        screenHeight = aScreenHeight;

        snowGen = new Random();

        start();
    }

    /**
     * On each "frame" of the animation, add another snowflake to the storm.
     */
    public void nextFrame() {
        snowStorm.add (new SnowFlake(snowPic, snowGen.nextInt(screenWidth), screenHeight,
                snowGen.nextInt(screenWidth) * 2 / screenWidth + 2));
    }
}

