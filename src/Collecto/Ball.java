package Collecto;

public enum Ball {
    BLUE, YELLOW, RED, ORANGE, PURPLE, GREEN, WHITE;

    public static final String ANSI_RESET = "\u001B[0m";

    public String getColour() {
        return this.name();
    }

    @Override
    public String toString() {
        if (this == BLUE) {
            return "\u001B[34m" + "BLUE" + ANSI_RESET;
        } else if (this == YELLOW) {
            return "\u001B[93m" + "YELLOW" + ANSI_RESET;
        } else if (this == RED) {
            return "\u001B[31m" + "RED" + ANSI_RESET;
        } else if (this == ORANGE) {
            return "\u001B[33m" + "ORANGE" + ANSI_RESET;
        } else if (this == PURPLE) {
            return "\u001B[35m" + "PURPLE" + ANSI_RESET;
        } else if (this == GREEN) {
            return "\u001B[32m" + "GREEN" + ANSI_RESET;
        } else {  // WHITE
            return "\u001B[37m" + "" + ANSI_RESET;
        }
//        return getColour();
    }
}

//public class Ball {
//    public Colour blue = new Colour("blue");
//    public Colour yellow = new Colour("yellow");
//    public Colour red = new Colour("red");
//    public Colour orange = new Colour("orange");
//    public Colour purple = new Colour("purple");
//    public Colour green = new Colour("green");
//    public Colour empty = new Colour("WHITE");
//
//    private final Colour colour;
//
//    /**
//     * @ensures a ball colour is set upon creation
//     */
//    public Ball(Colour colour) {
//        this.colour = colour;
//    }
//
//    public Colour getColour() {
//        return colour;
//    }
//
//    public static class Colour {
//        private final String name;
//
//        public Colour(String name) {
//            this.name = name;
//        }
//
//        public String getName() {
//            return name;
//        }
//
//    }
//
//}
