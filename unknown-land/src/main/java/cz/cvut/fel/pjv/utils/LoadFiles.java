package cz.cvut.fel.pjv.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.logging.Logger;
import java.io.InputStream;

/**
 * Class for loading types of files.
 * It is used to load images, subimages, txt files, and fonts.
 * 
 * @author Son Ngoc Tran
 */
public class LoadFiles {

    private static final Logger log = Logger.getLogger(LoadFiles.class.getName());

    /**
     * Load image from the file.
     * Save it as an Image.
     * 
     * @param sheet path to the image
     * @return image
     */
    public static Image LoadImage(String sheet) {
        URL url = LoadFiles.class.getResource(sheet);
        Image img = null;
        try {
            img = new Image(url.toString());
        } catch (Exception e) {
            log.warning("Error loading image: " + e.getMessage());
        }
        return img;
    }

    /**
     * Load subimages from the spritesheet.
     * Save them as 2D array of Images.
     * It is used for animations.
     * 
     * @param sheet
     * @param pixSize
     * @return image[][]
     */
    public static Image[][] LoadSubImages(String sheet, int pixSize) {
        Image img = LoadImage(sheet);

        PixelReader reader = img.getPixelReader();
        int width = (int) img.getWidth() / pixSize;
        int height = (int) img.getHeight() / pixSize;
        Image sprites[][] = new Image[height][width];

        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                sprites[j][i] = new WritableImage(reader, i * pixSize, j * pixSize, pixSize, pixSize);
            }
        }
        return sprites;

    }

    /**
     * Load txt file.
     * Save it as 2D array of integers.
     * It's used for collision detection.
     * 
     * @param sheet path to the txt file
     * @return ArrayList<ArrayList<Integer>>
     */
    public static ArrayList<ArrayList<Integer>> LoadTxt(String sheet) {
        String line;
        char c;
        ArrayList<ArrayList<Integer>> collisionMap;
        ArrayList<Integer> row;

        // using arraylist inside of arraylist to store 2D array
        // we don't know the size of it
        collisionMap = new ArrayList<ArrayList<Integer>>();

        URL url = LoadFiles.class.getResource(sheet);

        try {
            InputStream is = url.openStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);

            while ((line = reader.readLine()) != null) {
                row = new ArrayList<Integer>();
                line = line.replaceAll("\\s", ""); // remove all whitespaces

                // adding each number from the line to the ArrayList row that is inside of the
                // ArrayList collisionMap
                for (int i = 0; i < line.length(); i++) {
                    c = line.charAt(i); // get character at index of String line
                    row.add(Character.getNumericValue(c)); // change ASCII value of number to normal integer
                }
                collisionMap.add(row);
            }

            reader.close();
        } catch (IOException e) {
            log.warning("Error loading txt file: " + e.getMessage());
        }

        return collisionMap;
    }

    /**
     * Load font from the file.
     * Save it as a Font.
     * 
     * @param sheet    path to the font
     * @param fontSize size of the font
     * @return font
     */
    public static Font LoadFont(String sheet, int fontSize) {
        Font font = null;
        try {
            InputStream is = LoadFiles.class.getResourceAsStream(sheet); // for some reason, URL doesn't work
            font = Font.loadFont(is, fontSize);
            if (font == null) {
                log.warning("Error loading font: Font not loaded");
                throw new Exception("Font not loaded");
            }
        } catch (Exception e) {
            log.warning("Error loading font: " + e.getMessage());
        }
        return font;
    }
}
