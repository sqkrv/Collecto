package Collecto;

/**
 * This class is made for text colouring
 * and consists of string of colour codes
 * and methods to set the colour of a string.
 * @see TUI
 * @see Ball
 * <p>This class uses ANSI escape codes to handle colouring of the strings.
 */
public class Colour {
    public static final String RESET = "\u001B[0m";

    public static final String BLACK = "\u001B[30m";
    public static final String GREY = "\u001B[37m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[93m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String ORANGE = "\u001B[33m";

    public static String black(String string) {
        return BLACK + string + RESET;
    }

    public static String grey(String string) {
        return GREY + string + RESET;
    }

    public static String red(String string) {
        return RED + string + RESET;
    }

    public static String green(String string) {
        return GREEN + string + RESET;
    }

    public static String yellow(String string) {
        return YELLOW + string + RESET;
    }

    public static String blue(String string) {
        return BLUE + string + RESET;
    }

    public static String purple(String string) {
        return PURPLE + string + RESET;
    }

    public static String cyan(String string) {
        return CYAN + string + RESET;
    }

    public static String orange(String string) {
        return ORANGE + string + RESET;
    }
}
