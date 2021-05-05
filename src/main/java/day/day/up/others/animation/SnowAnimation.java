package day.day.up.others.animation;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

/**
 * Runs an animation that displays a snowstorm.
 * @author Barbara Lerner
 * @version Feb 10, 2010
 *
 */
public class SnowAnimation extends Animation {

    // dimensions of the screen
    private static final int SCREEN_HEIGHT = 400;
    private static final int SCREEN_WIDTH = 500;

    // constatnts for the moon size and location
    private static final int MOON_INSET = 50;
    private static final int MOON_SIZE = 70;

    // sizes and locations for the bricks
    private static final int BRICK_WIDTH = 30;
    private static final int BRICK_SPACING = 3;

    // colors of the sky, mortar, and brick
    private final Color NIGHTSKY = new Color(50, 50, 100);
    private final Color MORTAR = new Color(200, 200, 200);
    private final Color BRICKRED = new Color(150, 40, 40);

    // The snowflakes that have been created.
    private static final int MAX_FLAKES = 1000;
    private SnowFlake[] flakes = new SnowFlake[MAX_FLAKES];

    // The number of snowflakes created so far.
    private int numFlakes = 0;

    // Create a cloud, which drops snowflakes.
    public SnowAnimation(ImageIcon aSnowPic, int x,
                         int aScreenHeight) {
        Cloud cloud = new Cloud(this, aSnowPic, x, aScreenHeight);
    }

    /**
     * Update the state of the animation by having each snowflake
     * update its state.
     */
    public void nextFrame() {
        for (int i = 0; i < numFlakes; i++) {
            flakes[i].nextFrame();
        }
    }

    /**
     * Draw the current frame.  This consists of the background sky,
     * moon and sidewalk as well as all the snowflakes.
     */
    public void paint (Graphics g) {
        Dimension d = getSize();
        int brickLine = d.height*85/100;
        int brickHeight = d.height*4/100;
        int skyline = brickLine + brickHeight;

        // draw solid sky, mortar, and moon
        g.setColor(NIGHTSKY);
        g.fillRect(0, 0, d.width, skyline);
        g.setColor(MORTAR);
        g.fillRect(0, skyline, d.width, d.height - brickLine);
        g.setColor(Color.WHITE);
        g.fillOval(MOON_INSET, MOON_INSET, MOON_SIZE, MOON_SIZE);

        // add the bricks
        int brickPosition = 0;
        g.setColor(BRICKRED);
        while (brickPosition < d.width) {
            g.fillRect(brickPosition, brickLine, BRICK_WIDTH,
                    brickHeight);

            brickPosition = brickPosition + BRICK_WIDTH + BRICK_SPACING;
        }

        // Draw the snowflakes.
        for (int i = 0; i < numFlakes; i++) {
            flakes[i].paint(this, g);
        }
    }

    /**
     * Add a snowflake to the animation
     * @param flake the snowflake to add.  Should not be null.
     * 	  Does nothing if the snowflake array is full.
     */
    public void add (SnowFlake flake) {
        assert flake != null;
        if (numFlakes < MAX_FLAKES) {
            flakes[numFlakes] = flake;
            numFlakes++;
        }
    }

    // Creates the user interface
    public static void main(String[] args) {
        // Create the window and set its size.
        JFrame f = new JFrame();
        f.setSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));

        // Exit the application when the user closes the frame.
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnowAnimation snowAnimation = new SnowAnimation(new ImageIcon("D:\\Development\\BackendStudy\\src\\main\\resources\\BackendStudyNotes\\animations\\images\\snow.gif"), SCREEN_WIDTH, SCREEN_HEIGHT);

        // Add the snow panel to the center of the window
        Container contentPane = f.getContentPane();
        contentPane.add(snowAnimation, BorderLayout.CENTER);

        // Display the window.
        f.setVisible(true);

        // Start the animation
        snowAnimation.start();
    }

}