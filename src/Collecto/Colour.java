package Collecto;

/**
 * This class is made for text colouring
 * and consists of string of colour codes
 * and methods to set the colour of a string.
 *
 * <p>This class uses ANSI escape codes to handle colouring of the strings.
 *
 * @see TUI
 * @see Ball
 */
public class Colour {
    public static final String RESET = "\u001B[0m";

    public static final String GREY = "\u001B[37m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[93m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";
    public static final String ORANGE = "\u001B[33m";

    /**
     * Turns the given string into a grey string and resets the colour at the end of the string.
     *
     * @param string given string to be coloured
     * @return the given string but coloured grey
     */
    public static String grey(String string) {
        return GREY + string + RESET;
    }

    /**
     * Turns the given string into a red string and resets the colour at the end of the string.
     *
     * @param string given string to be coloured
     * @return the given string but coloured red
     */
    public static String red(String string) {
        return RED + string + RESET;
    }

    /**
     * Turns the given string into a green string and resets the colour at the end of the string.
     *
     * @param string given string to be coloured
     * @return the given string but coloured green
     */
    public static String green(String string) {
        return GREEN + string + RESET;
    }

    /**
     * Turns the given string into a yellow string and resets the colour at the end of the string.
     *
     * @param string given string to be coloured
     * @return the given string but coloured yellow
     */
    public static String yellow(String string) {
        return YELLOW + string + RESET;
    }

    /**
     * Turns the given string into a blue string and resets the colour at the end of the string.
     *
     * @param string given string to be coloured
     * @return the given string but coloured blue
     */
    public static String blue(String string) {
        return BLUE + string + RESET;
    }

    /**
     * Turns the given string into a purple string and resets the colour at the end of the string.
     *
     * @param string given string to be coloured
     * @return the given string but coloured purple
     */
    public static String purple(String string) {
        return PURPLE + string + RESET;
    }

    /**
     * Turns the given string into a cyan string and resets the colour at the end of the string.
     *
     * @param string given string to be coloured
     * @return the given string but coloured cyan
     */
    public static String cyan(String string) {
        return CYAN + string + RESET;
    }

    /**
     * Turns the given string into a orange string and resets the colour at the end of the string.
     *
     * @param string given string to be coloured
     * @return the given string but coloured orange
     */
    public static String orange(String string) {
        return ORANGE + string + RESET;
    }
}
