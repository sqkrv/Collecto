package Collecto;

import static Collecto.Colour.*;

public enum Ball {
    WHITE, BLUE, YELLOW, RED, ORANGE, PURPLE, GREEN;

    /**
     *
     * @return String name of the colour of a ball
     */
    @Override
    public String toString() {
        if (this == WHITE) return "";
        else return this.name();
    }

    /**
     *
     * @return coloured String name of the ball
     */
    public String toColouredString() {
        if (this == BLUE) {
            return blue(this.toString());
        } else if (this == YELLOW) {
            return yellow(this.toString());
        } else if (this == RED) {
            return red(this.toString());
        } else if (this == ORANGE) {
            return orange(this.toString());
        } else if (this == PURPLE) {
            return purple(this.toString());
        } else if (this == GREEN) {
            return green(this.toString());
        } else {
            return Colour.WHITE + this.toString() + RESET;
        }
    }

    /**
     *
     * @return the ANSI colour of a ball
     */
    public String ballColour() {
        if (this == BLUE) {
            return Colour.BLUE;
        } else if (this == YELLOW) {
            return Colour.YELLOW;
        } else if (this == RED) {
            return Colour.RED;
        } else if (this == ORANGE) {
            return Colour.ORANGE;
        } else if (this == PURPLE) {
            return Colour.PURPLE;
        } else if (this == GREEN) {
            return Colour.GREEN;
        } else {
            return RESET;
        }
    }
}
