package Collecto;

import static Collecto.Colour.*;

public enum Ball {
    WHITE, BLUE, YELLOW, RED, ORANGE, PURPLE, GREEN;

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
            return RESET_COLOUR;
        }
    }
}
