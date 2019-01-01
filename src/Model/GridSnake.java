package Model;

import java.util.*;

public class GridSnake {
    private Deque<Point> snakeDeque = new LinkedList<>();
    private Set<Point> snakeSet = new HashSet<>();
    private Set<Point> stateDifferences = new HashSet<>();
    private int gridSizeX, gridSizeY;
    private Directions direction;
    private Point apple;

    public GridSnake(int gridSizeX, int gridSizeY, int startingSnakeSize, Directions dir) {
        this.gridSizeX = gridSizeX;
        this.gridSizeY = gridSizeY;
        changeDirection(dir);
        int startingX = gridSizeX/2;
        int startingY = gridSizeY/2;
        for (int i = 0; i < startingSnakeSize; i++) {
            addTailSegment(new Point(startingX + i * dir.reversed().getX(), startingY + i * dir.reversed().getY()));
        }
        spawnApple();
    }

    public boolean gameTick() {
        //check if the game is won
        //if yes, end with false
        //check if the game is lost
        //if yes, end with false
        //move the snake
        //check if the snake ate an apple
        //if no, end with true
        //if yes, elongate the snake and spawn a new apple
        stateDifferences.clear();
        if (isGameWon() || isGameLost()) {
            return false;
        }
        moveSnake();
        if (isAppleEaten()) {
            elongateSnake();
            spawnApple();
        }
        return true;
    }

    private boolean isGameWon() {
        return snakeSet.size() == gridSizeX * gridSizeY;
    }

    private boolean isGameLost() {
        return snakeDeque.size() != snakeSet.size();
    }

    public int getScore() {
        return snakeDeque.size() - 3;
    }

    private void moveSnake() {
        Point snakeHead = getSnakeHead();
        removeTailSegment();
        addHeadSegment(new Point(snakeHead.getX() + direction.getX(), snakeHead.getY() + direction.getY()));
    }

    private boolean isAppleEaten() {
        return getSnakeHead().equals(apple);
    }

    private void elongateSnake() {
        addHeadSegment(new Point(getSnakeHead().getX() + direction.getX(), getSnakeHead().getY() + direction.getY()));
    }

    private void spawnApple() {
        if (isGameWon()) {
            return;
        }
        Set<Point> freeFields = getGameField();
        freeFields.removeAll(snakeSet);
        Random r = new Random();
        int chosenField = r.nextInt(freeFields.size());
        int i = 0;
        for (Point p : freeFields) {
            if (i == chosenField) {
                apple = getGridPoint(p);
                stateDifferences.add(getGridPoint(p));
            }
            i++;
        }
    }

    private void addTailSegment(Point p) {
        stateDifferences.add(getGridPoint(p));
        snakeDeque.addLast(getGridPoint(p));
        snakeSet.add(getGridPoint(p));
    }

    private void addHeadSegment(Point p) {
        stateDifferences.add(getGridPoint(snakeDeque.peekFirst()));
        stateDifferences.add(getGridPoint(p));
        snakeDeque.addFirst(getGridPoint(p));
        snakeSet.add(getGridPoint(p));
    }

    private void removeTailSegment() {
        stateDifferences.add(snakeDeque.peekLast());
        snakeSet.remove(snakeDeque.pollLast());
    }

    private void removeHeadSegment() {
        stateDifferences.add(snakeDeque.peekFirst());
        snakeSet.remove(snakeDeque.pollFirst());
        stateDifferences.add(snakeDeque.peekFirst());
    }

    public void changeDirection(Directions dir) {
        //snakes turning 180 degrees would be absurd
        if (direction != dir.reversed()) {
            direction = dir;
        }
    }

    private Point getSnakeHead() {
        return snakeDeque.peekFirst();
    }

    private int getGridX(int x) {
        return (x + gridSizeX) % gridSizeX;
    }

    private int getGridY(int y) {
        return (y + gridSizeY) % gridSizeY;
    }

    private Point getGridPoint(Point p) {
        return new Point(getGridX(p.getX()), getGridY(p.getY()));
    }

    public int getGridSizeX() {
        return gridSizeX;
    }

    public int getGridSizeY() {
        return gridSizeY;
    }

    public int getFieldType(int x, int y) {
        //0 is an empty field
        //1 is a part of the snake that's not the head
        //2 is the head
        //3 is the apple
        Point p = new Point(x, y);
        if (p.equals(snakeDeque.peekFirst())) {
            return 2;
        }
        if (snakeSet.contains(p)) {
            return 1;
        }
        if (p.equals(apple)) {
            return 3;
        }
        return 0;
    }

    private Set<Point> getGameField() {
        Set<Point> result = new HashSet<>();
        for (int y = 0; y < getGridSizeY(); y++) {
            for (int x = 0; x < getGridSizeX(); x++) {
                result.add(new Point(x, y));
            }
        }
        return result;
    }

    public Set<Point> getStateDifferences() {
        return stateDifferences;
    }
}
