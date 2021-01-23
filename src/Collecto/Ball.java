package Collecto;

public enum Ball {
    WHITE, BLUE, YELLOW, RED, ORANGE, PURPLE, GREEN;

    private static final String RESET_COLOUR = "\u001B[0m";

    public String getColour() {
        return this.name();
    }

    @Override
    public String toString() {
        if (this == WHITE) return "";
        else return getColour();
    }

    public String toColouredString() {
        if (this == BLUE) {
            return "\u001B[34m" + "BLUE" + RESET_COLOUR;
        } else if (this == YELLOW) {
            return "\u001B[93m" + "YELLOW" + RESET_COLOUR;
        } else if (this == RED) {
            return "\u001B[31m" + "RED" + RESET_COLOUR;
        } else if (this == ORANGE) {
            return "\u001B[33m" + "ORANGE" + RESET_COLOUR;
        } else if (this == PURPLE) {
            return "\u001B[35m" + "PURPLE" + RESET_COLOUR;
        } else if (this == GREEN) {
            return "\u001B[32m" + "GREEN" + RESET_COLOUR;
        } else {
            return "\u001B[37m" + "" + RESET_COLOUR;
        }
    }

    public String ballColour() {
        if (this == BLUE) {
            return "\u001B[34m";
        } else if (this == YELLOW) {
            return "\u001B[93m";
        } else if (this == RED) {
            return "\u001B[31m";
        } else if (this == ORANGE) {
            return "\u001B[33m";
        } else if (this == PURPLE) {
            return "\u001B[35m";
        } else if (this == GREEN) {
            return "\u001B[32m";
        } else {
            return RESET_COLOUR;
        }
    }
}
