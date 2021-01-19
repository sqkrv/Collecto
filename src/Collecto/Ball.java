package Collecto;

public enum Ball {
    BLUE, YELLOW, RED, ORANGE, PURPLE, GREEN, WHITE;

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
}
