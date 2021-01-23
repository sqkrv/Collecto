package Collecto;

public class Move {
    private final GridBoard.Direction direction;
    private final int line;

    public Move(int line, GridBoard.Direction direction) {
        assert 0 <= line && line < 7;
        this.line = line;
        this.direction = direction;
    }

    public Move(int push) {
        assert 0 <= push && push <= 27;
        this.line = push % 7;
        switch (push / 7) {
            case 0:
                direction = GridBoard.Direction.LEFT;
                break;
            case 1:
                direction = GridBoard.Direction.RIGHT;
                break;
            case 2:
                direction = GridBoard.Direction.UP;
                break;
            case 3:
                direction = GridBoard.Direction.DOWN;
                break;
            default:
                direction = null;
        }
    }

    public GridBoard.Direction getDirection() {
        return direction;
    }

    public int getLine() {
        return line;
    }

    public int push() {
        return line + direction.ordinal()*7;
    }

    @Override
    public String toString() {
        return (line+1)+" "+direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return line == move.line && direction == move.direction;
    }
}
