package net.exsource.openutils.enums;

import org.jetbrains.annotations.NotNull;

public enum Colors {

    //Basic Colors (CSS3 Supported)
    BLACK("#000000"),
    WHITE("#FFFFFF"),
    GRAY("#808080"),
    SILVER("#C0C0C0"),
    MAROON("#800000"),
    RED("#FF0000"),
    PURPLE("#800080"),
    FUCHSIA("#FF00FF"),
    GREEN("#008000"),
    LIME("#00FF00"),
    OLIVE("#808000"),
    YELLOW("#FFFF00"),
    NAVY("#000080"),
    BLUE("#0000FF"),
    TEAL("#008080"),
    AQUA("#00FFFF"),

    //Extended Colors (CSS3 Supported)

    ALICEBLUE("#f0f8ff"),
    ANTIQUEWHITE("#faebd7"),
    AQUAMARINE("#7fffd4"),
    AZURE("#f0ffff"),
    BEIGE("#f5f5dc"),
    BISQUE("#ffe4c4"),
    BLANCHEDALMOND("#ffebcd"),
    BLUEVIOLET("#8a2be2"),
    BROWN("#a52a2a"),
    BURLYWOOD("#deb887"),
    CADETBLUE("#5f9ea0"),
    CHARTREUSE("#7fff00"),
    CHOCOLATE("#d2691e"),
    CORAL("#ff7f50"),
    CORNFLOWERBLUE("#6495ed"),
    CORNSILK("#fff8dc"),
    CRIMSON("#dc143c"),
    CYAN("#00ffff"),
    DARKBLUE("#00008b"),
    DARKCYAN("#008b8b"),
    DARKGOLDENROD("#b8860b"),
    DARKGRAY("#a9a9a9"),
    DARKGREEN("#006400"),
    DARKGREY("#a9a9a9"),
    DARKKHAKI("#bdb76b"),
    DARKMAGENTA("#8b008b"),
    DARKOLIVEGREEN("#556b2f"),
    DARKORANGE("#ff8c00"),
    DARKORCHID("#9932cc"),
    DARKRED("#8b0000"),
    DARKSALMON("#e9967a"),
    DARKSEAGREEN("#8fbc8f"),
    DARKSLATEBLUE("#483d8b"),
    DARKSLATEGRAY("#2f4f4f"),
    DARKSLATEGREY("#2f4f4f"),
    DARKTURQUOISE("#00ced1"),
    DARKVIOLET("#9400d3"),
    DEEPPINK("#ff1493");

    private final String hexadecimal;

    Colors(final String hexadecimal) {
        this.hexadecimal = hexadecimal;
    }

    public final String getName() {
        return name().toLowerCase();
    }

    public final String getHexadecimal() {
        return hexadecimal;
    }

    public static Colors get(@NotNull String hexadecimal) {
        Colors color = Colors.BLACK;
        for(Colors colors : values()) {
            if(colors.getHexadecimal().equals(hexadecimal)) {
                color = colors;
                break;
            }
        }
        return color;
    }
}
