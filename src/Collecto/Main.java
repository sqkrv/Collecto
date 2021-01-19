package Collecto;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) System.out.println("die");

        Player player1;
        Player player2;
        String name;
        Scanner input = new Scanner(System.in);

        System.out.println("p1 name");
        name = input.nextLine();
//        player1 = new HumanPlayer(name);

        System.out.println("p2 name");
        name = input.nextLine();
//        player2 = new HumanPlayer(name);

//        Game game = new Game(player1, player2);
//        game.start();
    }
}
