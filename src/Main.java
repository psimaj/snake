import Control.MenuController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("View/menu.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("Snake menu");
        primaryStage.setScene(new Scene(root, 300, 220));
        primaryStage.setResizable(false);
        MenuController controller = loader.getController();
        primaryStage.setOnHidden(event -> controller.quitAllGames());
        primaryStage.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() == KeyCode.ENTER) {
                controller.startNewGame();
                event.consume();
            }
        });
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
