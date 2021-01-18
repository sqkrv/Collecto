package Collecto;

public enum Ball {
    BLUE, YELLOW, RED, ORANGE, PURPLE, GREEN, WHITE;

    static final String ANSI_RESET = "\u001B[0m";

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
    }
}
