package Collecto;

public class TUI {
//    private final String

    public static void main(String[] args) {
//        System.out.print("     |");
//        for (int i = 1; i <= 7; i++) {
//            System.out.print("       |");
//        }
        System.out.println();
        System.out.print("     |");
        for (int i = 1; i <= 7; i++) {
            System.out.print("   ");
            System.out.print(i);
            System.out.print("   |");
        }

        System.out.println();

        for (int i = 1; i <= 7; i++) {
            System.out.print("-----+");
            for (int j = 1; j <= 7; j++) {
                System.out.print("-------+");
            }
            System.out.println();
            System.out.print("  "+i+"  |");
            for (int j = 1; j <= 7; j++) {
                System.out.printf("%7s|", "");
            }
            System.out.println();
        }
        System.out.print("-----+");
        for (int j = 1; j <= 7; j++) {
            System.out.print("-------+");
        }
    }
}
