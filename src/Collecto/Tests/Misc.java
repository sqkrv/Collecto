package Collecto.Tests;

import Collecto.Ball;

import java.util.ArrayList;
import java.util.Arrays;

public class Misc {
    public static ArrayList<ArrayList<Ball>> sampleBoardArray() {
        ArrayList<ArrayList<Ball>> sampleBoardArray = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            sampleBoardArray.add(i, new ArrayList<>());
        }
        sampleBoardArray.get(0).addAll(
                Arrays.asList(
                        Ball.GREEN,
                        Ball.YELLOW,
                        Ball.ORANGE,
                        Ball.GREEN,
                        Ball.PURPLE,
                        Ball.RED,
                        Ball.ORANGE
                )
        );
        sampleBoardArray.get(1).addAll(
                Arrays.asList(
                        Ball.YELLOW,
                        Ball.BLUE,
                        Ball.RED,
                        Ball.ORANGE,
                        Ball.YELLOW,
                        Ball.GREEN,
                        Ball.PURPLE
                )
        );
        sampleBoardArray.get(2).addAll(
                Arrays.asList(
                        Ball.BLUE,
                        Ball.RED,
                        Ball.PURPLE,
                        Ball.BLUE,
                        Ball.RED,
                        Ball.YELLOW,
                        Ball.ORANGE
                )
        );
        sampleBoardArray.get(3).addAll(
                Arrays.asList(
                        Ball.PURPLE,
                        Ball.YELLOW,
                        Ball.GREEN,
                        Ball.WHITE,
                        Ball.BLUE,
                        Ball.RED,
                        Ball.GREEN
                )
        );
        sampleBoardArray.get(4).addAll(
                Arrays.asList(
                        Ball.ORANGE,
                        Ball.GREEN,
                        Ball.BLUE,
                        Ball.GREEN,
                        Ball.PURPLE,
                        Ball.BLUE,
                        Ball.RED
                )
        );
        sampleBoardArray.get(5).addAll(
                Arrays.asList(
                        Ball.YELLOW,
                        Ball.PURPLE,
                        Ball.ORANGE,
                        Ball.YELLOW,
                        Ball.ORANGE,
                        Ball.YELLOW,
                        Ball.PURPLE
                )
        );
        sampleBoardArray.get(6).addAll(
                Arrays.asList(
                        Ball.ORANGE,
                        Ball.GREEN,
                        Ball.BLUE,
                        Ball.RED,
                        Ball.BLUE,
                        Ball.PURPLE,
                        Ball.RED
                )
        );
        return sampleBoardArray;
    }

    public static ArrayList<ArrayList<Ball>> copyArray(ArrayList<ArrayList<Ball>> array) {
        ArrayList<ArrayList<Ball>> copy_array = new ArrayList<>();

        for (ArrayList<Ball> row : array) {
            copy_array.add(new ArrayList<>(row));
        }

        return copy_array;
    }

    public static ArrayList<ArrayList<Ball>> emptyBoardArray() {
        ArrayList<ArrayList<Ball>> emptyBoardArray = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            emptyBoardArray.add(new ArrayList<>());
            for (int j = 0; j < 7; j++) {
                emptyBoardArray.get(i).add(Ball.WHITE);
            }
        }
        return emptyBoardArray;
    }
}
