package Model;

public enum Directions{
    UP(0, -1),
    DOWN(0, 1),
    LEFT(-1, 0),
    RIGHT(1, 0);

    private Point p;
    Directions(int x, int y) {
        p = new Point(x, y);
    }

    public Integer getX() {
        return p.getX();
    }

    public Integer getY() {
        return p.getY();
    }

    public Directions reversed() {
        if (this == UP) {
            return DOWN;
        }
        if (this == DOWN) {
            return UP;
        }
        if (this == LEFT) {
            return RIGHT;
        }
        return LEFT;
    }
}
