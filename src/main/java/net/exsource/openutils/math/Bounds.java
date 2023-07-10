package net.exsource.openutils.math;

/**
 * Bounds gives us a hit-box for objects such as elements.
 * The hit-box is calculated as a perimeter: x + y + (x + width) + (y + height).
 * The Bounds is required for triggering events.
 * @since 1.2.0
 * @author Daniel Ramke
 */
@SuppressWarnings("unused")
public class Bounds {

    private double startX;
    private double startY;
    private double endX;
    private double endY;

    /**
     * Constructor created a new bounds for an element.
     * All values as double.
     * @param startX the original x position.
     * @param startY the original y position.
     * @param endX the end position of x + width.
     * @param endY the end position of y + height.
     */
    public Bounds(double startX, double startY, double endX, double endY) {
        this.set(startX, startY, endX, endY);
    }

    /**
     * Constructor created a new bounds for an element.
     * All values as integer.
     * @param startX the original x position.
     * @param startY the original y position.
     * @param endX the end position of x + width.
     * @param endY the end position of y + height.
     */
    public Bounds(int startX, int startY, int endX, int endY) {
        this((double) startX, startY, endX, endY);
    }

    /**
     * Constructor change an existing bounds for an element.
     * All values as double.
     * @param startX the original x position.
     * @param startY the original y position.
     * @param endX the end position of x + width.
     * @param endY the end position of y + height.
     */
    public void set(double startX, double startY, double endX, double endY) {
        this.startX = startX;
        this.startY = startY;
        this.endX = endX;
        this.endY = endY;
    }

    /**
     * Function checks if another bounds inside these bounds.
     * @param other the bounds witch think is inside.
     * @return boolean - true if it inside, false if not.
     */
    public boolean inside(Bounds other) {
        boolean start = this.inside(other.getStartX(), other.getStartY());
        boolean end = this.inside(other.getEndX(), other.getEndY());
        return start && end;
    }

    /**
     * Function checks if an element witch it starts location inside these bounds.
     * @param x the screen x location.
     * @param y the screen y location.
     * @return boolean - true if it inside, false if not.
     */
    public boolean inside(double x, double y) {
        double thisX = getStartX();
        double thisY = getStartY();
        double thisWidth = getWidth();
        double thisHeight = getHeight();

        if(x >= thisX && x <= thisX + thisWidth) {
            return y >= thisY && y <= thisY + thisHeight;
        }
        return false;
    }

    /**
     * Function checks if the start and end locations inside these bounds.
     * @param startX the x start location.
     * @param startY the y start location.
     * @param endX the x end location.
     * @param endY the y end location.
     * @return boolean - true if it inside, false if not.
     */
    public boolean inside(double startX, double startY, double endX, double endY) {
        return this.inside(new Bounds(startX, startY, endX, endY));
    }

    /**
     * @return double - the bound width.
     */
    public double getWidth() {
        return this.endX - startX;
    }

    /**
     * @return double - the bound height.
     */
    public double getHeight() {
        return this.endY - startY;
    }

    /**
     * @return double - the start x location.
     */
    public double getStartX() {
        return this.startX;
    }

    /**
     * @return double - the start y location.
     */
    public double getStartY() {
        return this.startY;
    }

    /**
     * @return double - the end x location.
     */
    public double getEndX() {
        return this.endX;
    }

    /**
     * @return double - the end y location.
     */
    public double getEndY() {
        return this.endY;
    }

    /**
     * @return String - the bound as string.
     */
    @Override
    public String toString() {
        return "Bounds: { X=" + getStartX() + ", Y=" + getStartY() + ", EndX=" + getEndX() + ", EndY=" + getEndY()
                + ", Width=" + getWidth() + ", Height=" + getHeight() + " }";
    }

}
