package cz.cvut.fel.pjv.utils;

import java.io.Serializable;

/**
 * Class for the Vector2D.
 * It is used to store the x and y coordinates.
 * It's more convenient to store the coordinates in a class.
 * 
 * @author Son Ngoc Tran
 */
public class Vector2D implements Serializable {

    private double x;
    private double y;

    public Vector2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setVector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return this.x;
    }

    public double getY() {
        return this.y;
    }

}
