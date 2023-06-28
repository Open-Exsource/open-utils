package net.exsource.openutils.enums;

import org.jetbrains.annotations.NotNull;

public enum Colors {

    //Basic Colors (CSS3 Supported)
    BLACK("#000000"),
    WHITE("#FFFFFF"),
    GRAY("#808080"),
    GREY("#808080"),
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
    DEEPPINK("#ff1493"),
    DEEPSKYBLUE("#00bfff"),
    DIMGRAY("#696969"),
    DIMGREY("#696969"),
    DODGERBLUE("#1e90ff"),
    FIREBRICK("#b22222"),
    FLORALWHITE("#fffaf0"),
    FORESTGREEN("#228b22"),
    GAINSBORO("#dcdcdc"),
    GHOSTWHITE("f8f8ff"),
    GOLD("#ffd700"),
    GOLDENROD("#daa520"),
    GREENYELLOW("#adff2f"),
    HONEYDEW("#f0fff0"),
    HOTPINK("#ff69b4"),
    INDIANRED("#cd5c5c"),
    INDIGO("#4b0082"),
    IVORY("#fffff0"),
    KHAKI("#f0e68c"),
    LAVENDER("#e6e6fa"),
    LAVENDERBLUSH("#fff0f5"),
    LAWNGREEN("#7cfc00"),
    LEMONCHIFFON("#fffacd"),
    LIGHTBLUE("#add8e6"),
    LIGHTCORAL("#f08080"),
    LIGHTCYAN("#e0ffff"),
    LIGHTGOLDENRODYELLOW("#fafad2"),
    LIGHTGRAY("#d3d3d3"),
    LIGHTGREEN("#90ee90"),
    LIGHTGREY("#d3d3d3"),
    LIGHTPINK("#ffb6c1"),
    LIGHTSALMON("#ffa07a"),
    LIGHTSEAGREEN("#20b2aa"),
    LIGHTSKYBLUE("#87cefa"),
    LIGHTSLATEGRAY("#778899"),
    LIGHTSLATEGREY("#778899"),
    LIGHTSTEELBLUE("#b0c4de"),
    LIGHTYELLOW("#ffffe0"),
    LIMEGREEN("#32cd32"),
    LINEN("#faf0e6"),
    MAGENTA("#ff00ff"),
    MEDIUMAQUAMARINE("#66cdaa"),
    MEDIUMBLUE("#0000cd"),
    MEDIUMORCHID("#ba55d3"),
    MEDIUMPURPLE("#9370db"),
    MEDIUMSEAGREEN("#3cb371"),
    MEDIUMSLATEBLUE("#7b68ee"),
    MEDIUMSPRINGGREEN("#00fa9a"),
    MEDIUMTURQUOISE("#48d1cc"),
    MEDIUMVIOLETRED("#c71585"),
    MIDNIGHTBLUE("#191970"),
    MINTCREAM("#f5fffa"),
    MISTYROSE("#ffe4e1"),
    MOCCASIN("#ffe4b5"),
    NAVAJOWHITE("#ffdead"),
    OLDLACE("#fdf5e6"),
    OLIVEDRAB("#6b8e23"),
    ORANGE("#ffa500"),
    ORANGERED("#ff4500"),
    ORCHID("#da70d6"),
    PALEGOLDENROD("#eee8aa"),
    PALEGREEN("#98fb98"),
    PALETURQUOISE("#afeeee"),
    PALEVIOLETRED("#db7093"),
    PAPAYAWHIP("#ffefd5"),
    PEACHPUFF("#ffdab9"),
    PERU("#cd853f"),
    PINK("#ffc0cb"),
    PLUM("#dda0dd"),
    POWDERBLUE("#b0e0e6"),
    ROSYBROWN("#bc8f8f"),
    ROYALBLUE("#4169e1"),
    SADDLEBROWN("#8b4513"),
    SALMON("#fa8072"),
    SANDYBROWN("#f4a460"),
    SEAGREEN("#2e8b57"),
    SEASHELL("#fff5ee"),
    SIENNA("#a0522d"),
    SKYBLUE("#87ceeb"),
    SLATEBLUE("#6a5acd"),
    SLATEGRAY("#708090"),
    SLATEGREY("#708090"),
    SNOW("#fffafa"),
    SPRINGGREEN("#00ff7f"),
    STEELBLUE("#4682b4"),
    TAN("#d2b48c"),
    THISTLE("#d8bfd8"),
    TOMATO("#ff6347"),
    TURQUOISE("#40e0d0"),
    VIOLET("#ee82ee"),
    WHEAT("#f5deb3"),
    WHITESMOKE("#f5f5f5"),
    YELLOWGREEN("#9acd32");

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

    public static Colors getByName(@NotNull String colorName) {
        Colors color = Colors.BLACK;
        for(Colors colors : values()) {
            if(colors.getName().equalsIgnoreCase(colorName)) {
                color = colors;
                break;
            }
        }
        return color;
    }

    public static Colors getByColorCode(@NotNull String hexadecimal) {
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
