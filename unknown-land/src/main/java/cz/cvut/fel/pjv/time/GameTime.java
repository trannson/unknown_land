package cz.cvut.fel.pjv.time;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;

import cz.cvut.fel.pjv.utils.Vector2D;
import cz.cvut.fel.pjv.view.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * Class for the GameTime.
 * It is used to render the time in the game.
 * It's a thread to keep track of the time in the game.
 * Time of the game is its own time, not the real time.
 * 
 * @author Son Ngoc Tran
 */
public class GameTime implements Runnable {

    private int minutes, hours, clockWidth, clockHeight;
    private boolean running;
    private Thread thread;
    private String dayCycle;
    private Vector2D clockOffset;

    /**
     * Constructor for the GameTime class.
     * Initialize the time to 0:00 AM.
     * Start the thread.
     */
    public GameTime() {
        minutes = 0;
        hours = 0;
        dayCycle = "AM";
        running = true;
        clockWidth = 100;
        clockHeight = 40;
        clockOffset = new Vector2D(WINDOW_WIDTH - 120, 20);
        thread = new Thread(this); // new thread for time
        thread.start();
    }

    /**
     * Run method for the thread.
     * Every 200ms the minutes are increased by 1.
     * If minutes reach 60, they are set to 0 and hours are increased by 1.
     * If hours reach 12, the day cycle is changed from AM to PM or vice versa.
     */
    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            minutes++;
            if (minutes == 60) {
                minutes = 0;
                hours++;
            }
            if (hours == 12) {
                if (dayCycle.equals("AM")) {
                    dayCycle = "PM";
                } else {
                    dayCycle = "AM";
                }
                hours = 1;
            }

        }
    }

    /**
     * Stop the time.
     */
    public void stopTime() {
        running = false;
    }

    /**
     * Render the time on the screen.
     * 
     * @param gc
     * @param camera used for positioning the time on the screen
     */
    public void render(GraphicsContext gc, Camera camera) {
        double placementX = camera.getX() + clockOffset.getX();
        double placementY = camera.getY() + clockOffset.getY();

        gc.setFill(Color.rgb(182, 182, 182, 0.7));
        gc.fillRect(placementX, placementY, clockWidth, clockHeight);

        gc.setStroke(Color.rgb(45, 46, 51, 0.8));
        gc.setLineWidth(4);
        gc.strokeRect(placementX, placementY, clockWidth, clockHeight);

        drawTime(gc, placementX, placementY);
    }

    /**
     * Draw the time on the screen with the correct format.
     * 
     * @param gc
     * @param placementX
     * @param placementY
     */
    private void drawTime(GraphicsContext gc, double placementX, double placementY) {
        String time = String.format("%02d:%02d %s", hours, minutes, dayCycle);
        gc.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        gc.setFill(Color.BLACK);
        gc.fillText(time, placementX + 7, placementY + 28);
    }

    /**
     * Set the time if needed.
     * 
     * @param hours
     * @param minutes
     * @param dayCycle
     */
    public void setTime(int hours, int minutes, String dayCycle) {
        if (hours >= 0 && hours <= 12 && minutes >= 0 && minutes <= 60) {
            this.hours = hours;
            this.minutes = minutes;
            this.dayCycle = dayCycle;
        }

    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public String getDayCycle() {
        return dayCycle;
    }

}
