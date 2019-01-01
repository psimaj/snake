package Control;

import Model.Directions;
import Model.GridSnake;
import Model.Point;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameController {
    public GridPane gameField;
    public Text gameStateInfo;
    public HBox gameStateInfoWrapper;
    private GridSnake gridSnake;
    private Pane[][] fields;
    private ScheduledExecutorService gameRunner;
    private String gameEndedInfo = "";
    private boolean gameEnded;

    void secondInitialize(int sizeX, int sizeY, int frameTime, int fieldSizeX, int fieldSizeY) {
        int startingSize = 3;
        Directions dir = Directions.UP;

        fields = new Pane[sizeX][sizeY];
        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                fields[x][y] = new Pane();
                fields[x][y].setMaxHeight(fieldSizeY);
                fields[x][y].setMinHeight(fieldSizeY);
                fields[x][y].setMinWidth(fieldSizeX);
                fields[x][y].setMaxWidth(fieldSizeX);
                gameField.add(fields[x][y], x, y);
            }
        }
        gridSnake = new GridSnake(sizeX, sizeY, startingSize, dir);

        renderFullGameState();
        gameRunner = Executors.newSingleThreadScheduledExecutor();
        gameRunner.scheduleAtFixedRate(this::runGame, 0, frameTime, TimeUnit.MILLISECONDS);

        setKeyBindings();
        gameStateInfoWrapper.setStyle("-fx-background-color: lightgray");
    }

    private void setKeyBindings() {
        gameField.getScene().addEventFilter(KeyEvent.ANY, keyEvent -> {
            final KeyCodeCombination left = new KeyCodeCombination(KeyCode.LEFT);
            final KeyCodeCombination right = new KeyCodeCombination(KeyCode.RIGHT);
            final KeyCodeCombination up = new KeyCodeCombination(KeyCode.UP);
            final KeyCodeCombination down = new KeyCodeCombination(KeyCode.DOWN);
            if (left.match(keyEvent)) {
                changeDirection(Directions.LEFT);
            } else if (right.match(keyEvent)) {
                changeDirection(Directions.RIGHT);
            } else if (up.match(keyEvent)) {
                changeDirection(Directions.UP);
            } else if (down.match(keyEvent)) {
                changeDirection(Directions.DOWN);
            }
        });
    }

    private Directions nextDirection = Directions.UP;

    private void changeDirection(Directions dir) {
        nextDirection = dir;
    }

    void close() {
        gameRunner.shutdownNow();
    }

    synchronized private void runGame() {
        if (gameEnded) {
            gameRunner.shutdown();
            return;
        }
        gridSnake.changeDirection(nextDirection);
        if (!gridSnake.gameTick()) {
            gameEndedInfo = "GAME ENDED!";
            gameEnded = true;
            playDeathSound();
        }
        Platform.runLater(() -> {
            updateGameStateInfo();
            renderFullGameState();
        });
    }

    private void updateGameStateInfo() {
        gameStateInfo.setText(gameEndedInfo + " Score: " + gridSnake.getScore());
    }

    synchronized private void renderFullGameState() {
        for (int y = 0; y < gridSnake.getGridSizeY(); y++) {
            for (int x = 0; x < gridSnake.getGridSizeX(); x++) {
                fields[x][y].setStyle("-fx-background-color: " + fieldTypeToColor(gridSnake.getFieldType(x, y)));
            }
        }
    }

    /*
    Sadly, the efficient method of rendering only the change of states
    stops working when the GUI update starts lagging behind
     */
    @SuppressWarnings("unused")
    synchronized private void renderPartialGameState() {
        for (Point p : gridSnake.getStateDifferences()) {
            fields[p.getX()][p.getY()].setStyle("-fx-background-color: " + fieldTypeToColor(gridSnake.getFieldType(p.getX(), p.getY())));
        }
    }

    private void playDeathSound() {
        new Thread(() -> {
            String fileName = "/sounds/roblox-death-sound.mp3";
            MediaPlayer mp = new MediaPlayer(new Media(getClass().getResource(fileName).toString()));
            mp.play();
        }).start();
    }

    private String fieldTypeToColor(int fieldType) {
        switch (fieldType) {
            case 0:
                return "white";
            case 1:
                return "green";
            case 2:
                return "cadetblue";
            case 3:
                return "red";
            default:
                return "black";
        }
    }
}
