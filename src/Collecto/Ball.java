package Collecto;

import static Collecto.Colour.*;

/**
 * Enum class for all possible in-game balls.
 * The white ball represents an empty field on a board.
 *
 * @see Colour
 */
public enum Ball {
    WHITE, BLUE, YELLOW, RED, ORANGE, PURPLE, GREEN;

    /**
     * Returns string name of the colour of this ball.
     * If ball is WHITE returns empty string.
     * Returned colour string is all uppercase.
     *
     * @return string name of the colour of a ball, empty string if this ball is WHITE
     */
    @Override
    public String toString() {
        if (this == WHITE) {
            return "";
        } else {
            return this.name();
        }
    }

    /**
     * Returns string name of the colour of this ball but with applied ANSI colour.
     * If ball is WHITE returns empty string with white ANSI colour applied.
     *
     * @return coloured string of the name of this Ball, empty string if this ball is WHITE
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
     * Returns related ANSI code for this colour.
     * If ball is WHITE returns {@link Colour#RESET} code.
     *
     * @return the ANSI colour of this ball, or a colour reset code if this ball is WHITE
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
