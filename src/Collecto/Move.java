package Collecto;

/**
 * This class stores and implements moves.
 * It stores the direction and line of a move,
 * where the direction is left, right, up or
 * down, and the line is a row or a column.
 * It supports conversion into a push, which
 * is a move described as an integer between
 * 0 and 27, where 0-6 is left, 7-13 is right,
 * 14-20 is up, and 21-27 is down.
 * It also contains the Direction enum class.
 * It is used primarily by many classes,
 * primarily the Game and GridBoard classes.
 *
 * @see Direction
 * @see Game
 * @see GridBoard
 */
public class Move {
    /**
     * This class is made for easy and convenient Move arguments handling.
     * <p>This class can be constructed be either line and direction arguments or push argument.
     */
    private final Direction direction;
    private final int line;

    /**
     * Enums of all possible directions of the Move
     */
    public enum Direction {
        LEFT, RIGHT, UP, DOWN;
    }

    /**
     * Constructs a move with specified line and direction arguments.
     *
     * @requires line argument to be between 0 and 7
     * @requires direction argument to be valid direction, not null
     * @param line line of the move
     * @param direction direction of the move
     */
    public Move(int line, Direction direction) {
        assert 0 <= line && line < 7;
        assert direction != null;
        this.line = line;
        this.direction = direction;
    }

    /**
     * Constructs a move with specified push argument.
     *
     * @requires push argument to be between 0 and 27
     * @param push number of push (according to protocol)
     */
    public Move(int push) {
        assert 0 <= push && push <= 27;
        this.line = push % 7;
        switch (push / 7) {
            case 0:
                direction = Direction.LEFT;
                break;
            case 1:
                direction = Direction.RIGHT;
                break;
            case 2:
                direction = Direction.UP;
                break;
            case 3:
                direction = Direction.DOWN;
                break;
            default:
                direction = null;
        }
    }

    /**
     * Returns direction of this move.
     *
     * @ensures direction is a valid direction
     * @return direction of this move
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns line of this move.
     *
     * @ensures line is a valid line between 0 and 7
     * @return line of this move
     */
    public int getLine() {
        return line;
    }

    /**
     * Calculates and returns push representation of this move.
     *
     * @ensures returned push is a valid push according to protocol
     * @return push representation of this move
     */
    public int push() {
        return line + direction.ordinal()*7;
    }

    /**
     * Override of {@code toString} method which returns human string representation of the move
     * in format {@code line+1 direction} (<i>e.g. 4 RIGHT</i>).
     * This relies on string representation of the board specifically
     * on enumeration of the lines.
     *
     * <p>This method should be used whenever move should be printed to the user in human readable format
     *
     * @return human string representation of the move
     */
    @Override
    public String toString() {
        return (line+1)+" "+direction;
    }

    /**
     * Checks two moves for equality. If provided object is Move instance and
     * both line and direction are equal in both classes method returns true. Otherwise - false.
     *
     * @param o object to check equality with
     * @return true if {@code Object} is {@code Move} instance
     * and {@code line} and {@code direction} properties are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return line == move.line && direction == move.direction;
    }
}
