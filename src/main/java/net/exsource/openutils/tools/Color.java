package net.exsource.openutils.tools;

import net.exsource.openlogger.Logger;
import net.exsource.openutils.enums.Colors;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

@SuppressWarnings("unused")
public class Color {

    private static final Logger logger = Logger.getLogger();

    public static final Color transparent = new Color(0, 0, 0, 0);

    private int red;
    private int green;
    private int blue;
    private int alpha;
    private int primitive;
    private float[] hsb;
    private String hexadecimal;
    private Color named;

    public Color(@NotNull Colors named) {
        this.setHexadecimal(named.getHexadecimal());
    }

    /**
     * This constructor created a color from the primitive type.
     * Primitive numbers can be found online or can random set by the user.
     *
     * @param primitive the needed value.
     */
    public Color(int primitive) {
        this.set(primitive);
    }

    /**
     * This constructor created a color from the hexadecimal type.
     * Hexadecimal type from #FFF - #FFFFFFFF.
     *
     * @param hexadecimal your color hexadecimal.
     */
    public Color(@NotNull String hexadecimal) {
        this.setHexadecimal(hexadecimal);
    }

    /**
     * This constructor created a color from the given integers.
     * The range value is 0 - 255.
     *
     * @param red   component.
     * @param green component.
     * @param blue  component.
     */
    public Color(int red, int green, int blue) {
        this(red, green, blue, 255);
    }

    /**
     * This constructor created a color from the given doubles.
     * The range value is 0.0 - 1.0 (0.0 = 0%, 1.0 = 100%)
     *
     * @param red   component.
     * @param green component.
     * @param blue  component.
     */
    public Color(double red, double green, double blue) {
        this(red, green, blue, 1.0);
    }

    /**
     * This constructor created a color from the given floats.
     * The range value is 0.0f - 1.0f (0.0 = 0%, 1.0 = 100%)
     *
     * @param hue        component.
     * @param saturation component.
     * @param brightness component.
     */
    public Color(float hue, float saturation, float brightness) {
        this.convertHsbToRgb(hue, saturation, brightness);
    }

    /**
     * This constructor created a color from the given integers.
     * The range value is 0 - 255.
     *
     * @param red   component.
     * @param green component.
     * @param blue  component.
     * @param alpha component.
     */
    public Color(int red, int green, int blue, int alpha) {
        int rgba = ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF));

        valid(red, green, blue, alpha);
        this.set(rgba);
    }

    /**
     * This constructor created a color from the given doubles.
     * The range value is 0.0 - 1.0 (0.0 = 0%, 1.0 = 100%)
     *
     * @param red   component.
     * @param green component.
     * @param blue  component.
     * @param alpha component.
     */
    public Color(double red, double green, double blue, double alpha) {
        this((int) (red * 255), (int) (green * 255), (int) (blue * 255), (int) (alpha * 255));
    }

    /**
     * This method returned this object as a new one.
     *
     * @return this.
     */
    public Color copy() {
        return new Color(this.getPrimitive());
    }

    /**
     * This method set the new component values.
     *
     * @param red   component.
     * @param green component.
     * @param blue  component.
     * @param alpha component.
     */
    public void set(int red, int green, int blue, int alpha) {
        float percentRed = red / 255.0f;
        float percentGreen = green / 255.0f;
        float percentBlue = blue / 255.0f;
        float percentAlpha = alpha / 255.0f;

        this.setRed(red);
        this.setGreen(green);
        this.setBlue(blue);
        this.setAlpha(alpha);
        this.generatePrimitive(red, green, blue, alpha);
        this.convertRgbToHex(red, green, blue, alpha);
        this.convertRgbToHsb(red, green, blue);
    }

    /**
     * This method set all needed values from the primitive type.
     *
     * @param primitive the primitive value.
     */
    public void set(int primitive) {
        this.primitive = primitive;

        int red = (primitive >> 16) & 0xFF;
        int green = (primitive >> 8) & 0xFF;
        int blue = (primitive) & 0xFF;
        int alpha = (primitive >> 24) & 0xFF;

        this.set(red, green, blue, alpha);
    }

    /**
     * This method set all values from the given hexadecimal code.
     *
     * @param hexadecimal value to convert.
     */
    public void setHexadecimal(String hexadecimal) {
        hexadecimal = hexadecimal.toUpperCase();

        if (!(hexadecimal.startsWith("#"))) {
            hexadecimal = "#" + hexadecimal;
        }

        if (hexadecimal.length() == 9) {
            this.set(Integer.valueOf(hexadecimal.substring(1, 3), 16),
                    Integer.valueOf(hexadecimal.substring(3, 5), 16),
                    Integer.valueOf(hexadecimal.substring(5, 7), 16),
                    Integer.valueOf(hexadecimal.substring(7, 9), 16));
        } else if (hexadecimal.length() >= 7) {
            this.set(Integer.valueOf(hexadecimal.substring(1, 3), 16),
                    Integer.valueOf(hexadecimal.substring(3, 5), 16),
                    Integer.valueOf(hexadecimal.substring(5, 7), 16), 255);
        } else if (hexadecimal.length() >= 4) {
            String hexadecimalRed = hexadecimal.substring(1, 2);
            String hexadecimalGreen = hexadecimal.substring(2, 3);
            String hexadecimalBlue = hexadecimal.substring(3, 4);

            hexadecimalRed += hexadecimalRed;
            hexadecimalGreen += hexadecimalGreen;
            hexadecimalBlue += hexadecimalBlue;

            this.set(Integer.valueOf(hexadecimalRed, 16),
                    Integer.valueOf(hexadecimalGreen, 16),
                    Integer.valueOf(hexadecimalBlue, 16), 255);
        } else {
            logger.error("Cannot parse hexadecimal code [ " + hexadecimal + " ]");
            this.set(0, 0, 0, 255);
        }
        this.convertRgbToHex(getRed(), getGreen(), getBlue(), getAlpha());
    }

    /**
     * This method converted int values to hexadecimal strings.
     *
     * @param red   component.
     * @param green component.
     * @param blue  component.
     * @param alpha component.
     */
    public void convertRgbToHex(int red, int green, int blue, int alpha) {
        this.hexadecimal = "#" + Integer.toHexString(red)
                + Integer.toHexString(green)
                + Integer.toHexString(blue)
                + Integer.toHexString(alpha);
    }

    /**
     * This method converted rgb codes to hsb.
     *
     * @param red   component.
     * @param green component.
     * @param blue  component.
     */
    public void convertRgbToHsb(int red, int green, int blue) {
        float hue;
        float saturation;
        float brightness;

        if (this.hsb == null) {
            this.hsb = new float[3];
        }

        int cMax = Math.max(red, green);
        if (blue > cMax) {
            cMax = blue;
        }

        int cMin = Math.min(red, green);
        if (blue < cMin) {
            cMin = blue;
        }

        brightness = ((float) cMax) / 255.0f;
        if (cMax != 0) {
            saturation = ((float) (cMax - cMin)) / ((float) cMax);
        } else {
            saturation = 0;
        }

        if (saturation == 0) {
            hue = 0;
        } else {
            float redComponent = ((float) (cMax - red)) / ((float) (cMax - cMin));
            float greenComponent = ((float) (cMax - green)) / ((float) (cMax - cMin));
            float blueComponent = ((float) (cMax - blue)) / ((float) (cMax - cMin));
            if (red == cMax) {
                hue = blueComponent - greenComponent;
            } else if (green == cMax) {
                hue = 2.0f + redComponent - blueComponent;
            } else {
                hue = 4.0f + greenComponent - redComponent;
            }
            hue = hue / 6.0f;
            if (hue < 0) {
                hue = hue + 1.0f;
            }
        }

        hsb[0] = hue;
        hsb[1] = saturation;
        hsb[2] = brightness;
    }

    /**
     * This method converted hsb to rgb
     *
     * @param hue        component.
     * @param saturation component.
     * @param brightness component.
     */
    public void convertHsbToRgb(float hue, float saturation, float brightness) {
        int red = 0;
        int green = 0;
        int blue = 0;

        if (saturation == 0) {
            red = green = blue = (int) (brightness * 255.0f + 0.5f);
        } else {
            float tempH = (hue - (float) Math.floor(hue)) * 6.0f;
            float tempF = tempH - (float) Math.floor(tempH);
            float tempP = brightness * (1.0f - saturation);
            float tempQ = brightness * (1.0f - saturation * tempF);
            float tempT = brightness * (1.0f - (saturation * (1.0f - tempF)));

            switch ((int) tempH) {
                case 0 -> {
                    red = (int) (brightness * 255.0f + 0.5f);
                    green = (int) (tempT * 255.0f + 0.5f);
                    blue = (int) (tempP * 255.0f + 0.5f);
                }
                case 1 -> {
                    red = (int) (tempQ * 255.0f + 0.5f);
                    green = (int) (brightness * 255.0f + 0.5f);
                    blue = (int) (tempP * 255.0f + 0.5f);
                }
                case 2 -> {
                    red = (int) (tempP * 255.0f + 0.5f);
                    green = (int) (brightness * 255.0f + 0.5f);
                    blue = (int) (tempT * 255.0f + 0.5f);
                }
                case 3 -> {
                    red = (int) (tempP * 255.0f + 0.5f);
                    green = (int) (tempQ * 255.0f + 0.5f);
                    blue = (int) (brightness * 255.0f + 0.5f);
                }
                case 4 -> {
                    red = (int) (tempT * 255.0f + 0.5f);
                    green = (int) (tempP * 255.0f + 0.5f);
                    blue = (int) (brightness * 255.0f + 0.5f);
                }
                case 5 -> {
                    red = (int) (brightness * 255.0f + 0.5f);
                    green = (int) (tempP * 255.0f + 0.5f);
                    blue = (int) (tempQ * 255.0f + 0.5f);
                }
            }
        }
        this.set((red << 16), (green << 8), (blue), 0xff000000);
    }

    /**
     * This returned a primitive integer value witch is building by the red, green, blue and alpha component.
     *
     * @param red   component.
     * @param green component.
     * @param blue  component.
     * @param alpha component.
     */
    public void generatePrimitive(int red, int green, int blue, int alpha) {
        this.primitive = ((alpha & 0xFF) << 24) | ((red & 0xFF) << 16) | ((green & 0xFF) << 8) | ((blue & 0xFF));
    }

    /**
     * This method set the red component.
     *
     * @param red component.
     */
    public void setRed(int red) {
        if (red > 255) {
            red = 255;
        } else if (red < 0) {
            red = 0;
        }
        this.red = red;
    }

    /**
     * @return red as int value.
     */
    public int getRed() {
        return this.red;
    }

    /**
     * @return red as float value.
     */
    public float getPercentRed() {
        return this.red / 255f;
    }

    /**
     * This method set the green component.
     *
     * @param green component.
     */
    public void setGreen(int green) {
        if (green > 255) {
            green = 255;
        } else if (green < 0) {
            green = 0;
        }
        this.green = green;
    }

    /**
     * @return green as int value.
     */
    public int getGreen() {
        return this.green;
    }

    /**
     * @return green as float value.
     */
    public float getPercentGreen() {
        return this.green / 255f;
    }

    /**
     * This method set the blue component.
     *
     * @param blue component.
     */
    public void setBlue(int blue) {
        if (blue > 255) {
            blue = 255;
        } else if (blue < 0) {
            blue = 0;
        }
        this.blue = blue;
    }

    /**
     * @return blue as int value.
     */
    public int getBlue() {
        return this.blue;
    }

    /**
     * @return blue as float value.
     */
    public float getPercentBlue() {
        return this.blue / 255f;
    }

    /**
     * This method set the alpha component.
     *
     * @param alpha component.
     */
    public void setAlpha(int alpha) {
        if (alpha > 255) {
            alpha = 255;
        } else if (alpha < 0) {
            alpha = 0;
        }
        this.alpha = alpha;
    }

    /**
     * @return alpha as int value.
     */
    public int getAlpha() {
        return this.alpha;
    }

    /**
     * @return alpha as float value.
     */
    public float getPercentAlpha() {
        return this.alpha / 255f;
    }

    /**
     * Internal function,
     * this method check if the given components ready to use.
     *
     * @param red   component.
     * @param green component.
     * @param blue  component.
     * @param alpha component.
     */
    private void valid(int red, int green, int blue, int alpha) {
        boolean error = false;
        StringBuilder errorBuilder = new StringBuilder();

        if (alpha < 0 || alpha > 255) {
            error = true;
            errorBuilder.append(" Alpha");
        }

        if (red < 0 || red > 255) {
            error = true;
            errorBuilder.append(" Red");
        }

        if (green < 0 || green > 255) {
            error = true;
            errorBuilder.append(" Green");
        }

        if (blue < 0 || blue > 255) {
            error = true;
            errorBuilder.append(" Blue");
        }
        if (error) {
            logger.error("Color parameter outside of expected range [" + errorBuilder + " ]");
            valid(0, 0, 0, 255);
        }
    }

    /**
     * This print log is only shown if the user allow debugMode.
     * To enable the debug mode you need to call this function {@link Logger#enableDebug(boolean)}
     * at the beginning of your program sample #main(String[]) function.
     */
    public void debugPrint() {
        logger.debug("RED: " + getRed());
        logger.debug("RED PERCENT: " + getPercentRed());
        logger.debug("GREEN: " + getGreen());
        logger.debug("GREEN PERCENT: " + getPercentGreen());
        logger.debug("BLUE: " + getBlue());
        logger.debug("BLUE PERCENT: " + getPercentBlue());
        logger.debug("ALPHA: " + getAlpha());
        logger.debug("ALPHA PERCENT: " + getPercentAlpha());
        logger.debug("PREM: " + getPrimitive());
        logger.debug("HEX: " + getHexadecimal());
        logger.debug("HUE: " + getHsb()[0] + " SATURATION: " + getHsb()[1] + " BRIGHTNESS: " + getHsb()[2]);
    }

    /**
     * @return primitive value.
     */
    public int getPrimitive() {
        return this.primitive;
    }

    /**
     * @return hexadecimal value.
     */
    public String getHexadecimal() {
        return hexadecimal;
    }

    /**
     * @return the float array hsb.
     */
    public float[] getHsb() {
        return hsb;
    }

    public static Color named(Colors colors) {
        return new Color(colors);
    }

    /**
     * Create a new color from the given int values. The color range for rgb (0 - 255).
     * If the value outside the range, the result color will be different to the wish color!
     * The alpha component is by default 255 (MAX)
     *
     * @param red   the red component
     * @param green the green component
     * @param blue  the blue component
     * @return the finished color from the given components.
     */
    public static Color rgb(int red, int green, int blue) {
        return rgba(red, green, blue, 255);
    }

    /**
     * Create a new color from the given int values. The color range for rgba (0 - 255).
     * If the value outside the range, the result color will be different to the wish color!
     *
     * @param red   the red component
     * @param green the green component
     * @param blue  the blue component
     * @param alpha the alpha component
     * @return the finished color from the given components.
     */
    public static Color rgba(int red, int green, int blue, int alpha) {
        return new Color(red, green, blue, alpha);
    }

    /**
     * Create a new color from the given int values. The color range for rgb (0.0 - 1.0).
     * If the value outside the range, the result color will be different to the wish color!
     * The alpha component is by default 1.0 (MAX)
     *
     * @param red   the red component
     * @param green the green component
     * @param blue  the blue component
     * @return the finished color from the given components.
     */
    public static Color rgb(double red, double green, double blue) {
        return rgba(red, green, blue, 1.0);
    }

    /**
     * Create a new color from the given int values. The color range for rgba (0.0 - 1.0).
     * If the value outside the range, the result color will be different to the wish color!
     *
     * @param red   the red component
     * @param green the green component
     * @param blue  the blue component
     * @param alpha the alpha component
     * @return the finished color from the given components.
     */
    public static Color rgba(double red, double green, double blue, double alpha) {
        return new Color(red, green, blue, alpha);
    }

    /**
     * Creates a color from the given hsb values.
     * Note this method is raw and not all user use this, The reason is we have rgba support.
     *
     * @param hue        the color temperature
     * @param saturation the color saturation
     * @param brightness the color lightning
     * @return the finished color from the given hsb codes.
     */
    public static Color hsb(float hue, float saturation, float brightness) {
        return new Color(hue, saturation, brightness);
    }

    /**
     * Creates a color from the given hsb values.
     * Note this method is raw and not all user use this, The reason is we have rgba support.
     *
     * @param hsb an array with length 3;
     * @return the finished color from the given hsb codes.
     */
    public static Color hsb(float[] hsb) {
        if (hsb.length < 3) {
            return new Color(Colors.BLACK);
        }
        return hsb(hsb[0], hsb[1], hsb[2]);
    }

    /**
     * Creates a color from the given hexadecimal string value.
     *
     * @param hexadecimal e.g #FFFFFF for white color.
     * @return the finished color from the given hex code.
     */
    public static Color hexadecimal(String hexadecimal) {
        return new Color(hexadecimal);
    }

    /**
     * Create a new color form the primitive int value. This means you can create some color from all components.
     * The components (red green blue, alpha).
     *
     * @param primitive the primitive number like -100225667
     * @return the finished color from the given components.
     */
    public static Color primitive(int primitive) {
        return new Color(primitive);
    }

    /**
     * Creates a new color from a java Random. The color value is created by mixing the {@link Color#rgb(int, int, int)} method.
     *
     * @return the finished color from the random.
     */
    public static Color randomRGB() {
        Random random = new Random();
        return rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255));
    }

    /**
     * Create a new awt color from our color class.
     *
     * @param color - the byteterm color object for manipulate.
     * @return the finished awt color.
     */
    public static java.awt.Color getAsAWTColor(Color color) {
        return new java.awt.Color(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
    }

    /**
     * This static variable contains the overall fallback color.
     * You can use this, or you make your own fallback.
     */
    public static Color FALLBACK_COLOR = new Color(Colors.GRAY);
}
