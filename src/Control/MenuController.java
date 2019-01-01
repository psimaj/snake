package Control;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class MenuController {

    public Button startButton;
    public TextField heightField;
    public TextField widthField;
    public TextField timeField;
    public Text tutorialText;
    public GridPane menuGrid;

    private ArrayList<Stage> games;

    public void initialize() {
        games = new ArrayList<>();
        tutorialText.setWrappingWidth(270);
        heightField.setText("6");
        widthField.setText("6");
        timeField.setText("500");
    }

    @FXML
    public void startButtonPressed() {
        startNewGame();
    }

    private void startNewGame() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("View/game.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            games.add(stage);
            stage.setTitle("Snake game");
            stage.setResizable(false);
            int sizeX, sizeY, frameTime;
            try {
                sizeX = Integer.parseUnsignedInt(widthField.getText());
                sizeY = Integer.parseUnsignedInt(heightField.getText());
                frameTime = Integer.parseUnsignedInt(timeField.getText());
            } catch (NumberFormatException e) {
                return;
            }
            if (sizeX < 3 || sizeY < 3 || frameTime < 1) {
                return;
            }
            int windowSize = 400;
            int maxTileCount = sizeX > sizeY ? sizeX : sizeY;
            int tileSize = (windowSize + maxTileCount - 1)/maxTileCount;
            int windowSizeX = tileSize * sizeX, windowSizeY = tileSize * sizeY;
            stage.setScene(new Scene(root, windowSizeX, windowSizeY + 15));
            GameController controller = loader.getController();
            controller.secondInitialize(sizeX, sizeY, frameTime, tileSize, tileSize);
            stage.setOnHidden(event -> controller.close());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void quitAllGames() {
        for (Stage s : games) {
            s.close();
        }
        Platform.exit();
    }
}
