import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Game extends Application {
    private Puzzle puzzle = new Puzzle();
    private MasterGrid gui = new MasterGrid(puzzle);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Puzzle");

        Button checkButton = new Button("Check");
        checkButton.setOnAction(this::onCheck);
        Button cleanButton = new Button("Clean");
        cleanButton.setOnAction(this::onClean);

        HBox buttons = new HBox(checkButton, cleanButton);
        buttons.setAlignment(Pos.BOTTOM_CENTER);

        VBox root = new VBox(gui.getPane(), buttons);

        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();
    }

    private void onCheck(ActionEvent event) {
        boolean correct = gui.isCorrect();

        Alert alert;
        if (correct) {
            alert = new Alert(Alert.AlertType.INFORMATION, "You've completed the puzzle successfully.", ButtonType.FINISH);
            alert.setOnCloseRequest(e -> {
                try {
                    stop();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            });
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "The puzzle isn't finished yet: guesses are missing or incorrect.", ButtonType.OK);
        }

        alert.initModality(Modality.WINDOW_MODAL);
        alert.show();
    }

    private void onClean(ActionEvent event) {
        gui.clean();

        Alert alert = new Alert(Alert.AlertType.INFORMATION, "The puzzle has been cleared of incorrect guesses.", ButtonType.OK);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.show();
    }
}