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
 * It is used by many classes, primarily by
 * the Game and GridBoard classes.
 *
 * @see Game
 * @see GridBoard
 */
public class Move {
    private final Direction direction;
    private final int line;

    /**
     * Constructs a move with specified line and direction arguments.
     * This class is made for easy and convenient Move arguments handling.
     * <p>This class can be constructed to be either line and direction arguments
     * or a push argument.
     *
     * @param line      line of the move
     * @param direction direction of the move
     * @requires line argument to be between 0 and 7
     * @requires direction argument to be valid direction, not null
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
     * @param push number of push (according to protocol)
     * @requires push argument to be between 0 and 27
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
     * Returns the direction of this move.
     *
     * @return direction of this move
     * @ensures direction is a valid direction
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the line of this move.
     *
     * @return line of this move
     * @ensures line is a valid line between 0 and 7
     */
    public int getLine() {
        return line;
    }

    /**
     * Calculates and returns the push representation of this move.
     *
     * @return push representation of this move
     * @requires {@code direction != null}
     * @ensures returned push is a valid push according to protocol
     */
    public int push() {
        assert direction != null;
        return line + direction.ordinal() * 7;
    }

    /**
     * Overrides the {@code toString} method to return a more readable string representation
     * of the move in format {@code line+1 direction}, e.g. {@code 4 RIGHT}.
     * This relies on string representation of the board specifically
     * on enumeration of the lines.
     *
     * <p>This method should be used whenever move should be printed
     * to the user in a readable format.
     *
     * @return readable string representation of the move
     */
    @Override
    public String toString() {
        return (line + 1) + " " + direction;
    }

    /**
     * Checks two moves for equality. If the provided object is an instance of the Move class and
     * both line and direction are equal in both classes then the method returns true.
     * Otherwise it will return false.
     *
     * @param o object to check equality with
     * @return true if {@code Object} is a {@code Move} instance
     * and all {@code line} and {@code direction} properties are the same
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Move move = (Move) o;
        return line == move.line && direction == move.direction;
    }

    /**
     * Enum of all possible directions of a Move.
     */
    public enum Direction {
        LEFT, RIGHT, UP, DOWN
    }
}
