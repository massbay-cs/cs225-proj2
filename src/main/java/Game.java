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

        VBox buttons = new VBox(checkButton, cleanButton);
        buttons.setAlignment(Pos.BOTTOM_CENTER);

        HBox root = new HBox(gui.getPane(), buttons);

        primaryStage.setScene(new Scene(root, 300, 300));
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

    /*
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Puzzle");

        int numberOfCategories = 3;
        int numberOfProperties = 4;

        GridPane bigGrid = new GridPane();
        GridPane smallGrid1 = new GridPane();
        GridPane smallGrid2 = new GridPane();
        GridPane smallGrid3 = new GridPane();
        GridPane smallGrid4 = new GridPane();
        GridPane smallGrid5 = new GridPane();
        GridPane smallGrid6 = new GridPane();
        bigGrid.setAlignment(Pos.CENTER);
        bigGrid.setHgap(10);
        bigGrid.setVgap(10);
        bigGrid.setPadding(new Insets(25,25,25,25));

        Button cell = new Button("0");
        for (int i = 0; i<(numberOfProperties-1); i++) {
            for (int j = 0; j<(numberOfProperties-1); j++) {
                smallGrid1.add(cell, i, j);
                smallGrid2.add(cell, i, j);
                smallGrid3.add(cell, i, j);
                smallGrid4.add(cell, i, j);
                smallGrid5.add(cell, i, j);
                smallGrid6.add(cell, i, j);
            }
        }

        int counter = 0;
        for (int i = 0; i<(numberOfCategories-2); i++) {
            for (int j = 0; j<(numberOfCategories-2); j++) {
                if (counter == 0) {
                    bigGrid.add(smallGrid1, i, j);
                }
                else if (counter == 1) {
                    bigGrid.add(smallGrid2, i, j);
                }
                else if (counter == 2) {
                    bigGrid.add(smallGrid3, i, j);
                }
                else if (counter == 3) {
                    bigGrid.add(smallGrid4, i, j);
                }
                else if (counter == 4) {
                    bigGrid.add(smallGrid5, i, j);
                }
                else if (counter == 5) {
                    bigGrid.add(smallGrid6, i, j);
                }
                counter++;
            }
        }

        Button startOver = new Button("Start Over");
        Button hint = new Button("Hint");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BASELINE_CENTER);
        hbBtn.getChildren().add(hint);
        hbBtn.getChildren().add(startOver);
        bigGrid.add(hbBtn,1,10);

        primaryStage.setScene(new Scene(bigGrid, 300, 300));
        primaryStage.show();
    }
    */
}