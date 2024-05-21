package cz.cvut.fel.pjv.features;

import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_HEIGHT;
import static cz.cvut.fel.pjv.utils.Constants.GameConstants.WINDOW_WIDTH;

import java.util.logging.Level;
import java.util.logging.Logger;

import cz.cvut.fel.pjv.utils.Vector2D;
import cz.cvut.fel.pjv.view.Camera;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/**
 * Quests class is used to display quests on the screen 
 * so the player knows what to do next.
 * They are displayed bottom right corner of the screen.
 * 
 * @author Son Ngoc Tran
 */
public class Quests {
    
    private Camera camera;
    private int questNumber, maxQuestNumber;
    private Logger log;
    private Vector2D questPosOffset;
    
    /**
     * Constructor of the Quests class.
     * @param camera used for x and y coordinates of the quests.
     */
    public Quests(Camera camera) {
        this.camera = camera;
        questNumber = 0;
        maxQuestNumber = 6;
        questPosOffset = new Vector2D(190, 20);
        log = Logger.getLogger(Quests.class.getName());
        log.setLevel(Level.INFO);
    }

    public void render(GraphicsContext gc) {
        drawQuest(gc);
    }

    /**
     * Draw the quest on the screen.
     * Depending on the quest number, different quest is displayed.
     * @param gc
     */
    private void drawQuest(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(Font.font("Verdana", 10));
        double x = camera.getX() + WINDOW_WIDTH - questPosOffset.getX();
        double y = camera.getY() + WINDOW_HEIGHT - questPosOffset.getY();

        switch (questNumber) {
            case 0:
                gc.fillText("Quest 1: Enter dungeon", x, y);
                break;
            case 1:
                gc.fillText("Quest 2: Steal ITEMS!", x, y);
                break;
            case 2:
                gc.fillText("Quest 3: Sell ITEMS", x, y);
                break;
            case 3:
                gc.fillText("Quest 4: Buy a MAP & SWORD", x, y);
                break;
            case 4:
                gc.fillText("Quest 5: Use Map", x, y);
                break;
            case 5:
                gc.fillText("Quest 6: Travel to Lava Planet", x, y);
                break;
            case 6:
                gc.fillText("Quest 7: KILL THE NECROMANCER!", x, y);
                break;
            default:
                log.warning("Quest number out of bounds");
                break;
        }
    }

    public void increaseQuestNumber() {
        if (questNumber < maxQuestNumber) {
            questNumber++;
        } else {
            log.warning("Quest number out of bounds");
        }
    }

    public void decreaseQuestNumber() {
        if (questNumber > 0) {
            questNumber--;
        } else {
            log.warning("Quest number out of bounds");
        }
    }

    public int getQuestNumber() {
        return questNumber;
    }

    public void setQuestNumber(int questNumber) {
        if (questNumber >= 0 && questNumber <= maxQuestNumber) {
            this.questNumber = questNumber;
        } else {
            log.warning("Quest number out of bounds");
        }
    }

}
