package Collecto;

public class Ball {
    public Colour blue = new Colour("blue");
    public Colour yellow = new Colour("yellow");
    public Colour red = new Colour("red");
    public Colour orange = new Colour("orange");
    public Colour purple = new Colour("purple");
    public Colour green = new Colour("green");
    public Colour empty = new Colour("WHITE");

    private final Colour colour;

    /**
     * @ensures a ball colour is set upon creation
     */
    public Ball(Colour colour) {
        this.colour = colour;
    }

    public Colour getColour() {
        return colour;
    }

    public static class Colour {
        private final String name;

        public Colour(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }

}
