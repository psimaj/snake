package Model;

import javafx.util.Pair;

import java.util.Random;

public class Point extends Pair<Integer, Integer> {
    public Point(Integer x, Integer y) {
        super(x, y);
    }

    public Integer getX() {
        return super.getKey();
    }

    public Integer getY() {
        return super.getValue();
    }

    public Point move(Directions dir) {
        return new Point(this.getX() + dir.getX(), this.getY() + dir.getY());
    }

    private static Random randSource = new Random();

    public static Point getRandomPoint(int boundX, int boundY) {
        return new Point(randSource.nextInt(boundX), randSource.nextInt(boundY));
    }
}
