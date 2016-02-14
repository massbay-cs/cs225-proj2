import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class Game extends Application {
    private Puzzle puzzle = new Puzzle();
    private MasterGrid gui = new MasterGrid(puzzle);
    private int cleanedMistakes = 0;
    private int hints = 0;
    private int attempts = 0;
    private long startTime;
    private long endTime;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Puzzle");

        double gap = 10;
        double clueWidth = 200 + gap * 2;
        double buttonHeight = 31;
        double height = gui.getHeight() + buttonHeight + gap * 2;
        double width = gui.getWidth() + clueWidth;

        Insets buttonInsets = new Insets(5, 8, 5, 8);
        Button checkButton = new Button("Check");
        checkButton.setTooltip(new Tooltip("Test whether you've successfully completed the puzzle."));
        checkButton.setPadding(buttonInsets);
        checkButton.setOnAction(this::onCheck);
        Button cleanButton = new Button("Clean");
        cleanButton.setTooltip(new Tooltip("Clear any incorrect markings from the grid."));
        cleanButton.setPadding(buttonInsets);
        cleanButton.setOnAction(this::onClean);
        Button hintButton = new Button("Hint");
        hintButton.setTooltip(new Tooltip("Receive one correct answer that you haven't yet marked."));
        hintButton.setPadding(buttonInsets);
        hintButton.setOnAction(this::onHint);
        Button helpButton = new Button("Help");
        helpButton.setTooltip(new Tooltip("Receive detailed help."));
        helpButton.setPadding(buttonInsets);
        helpButton.setOnAction(this::onHelp);

        HBox buttons = new HBox(gap, checkButton, cleanButton, hintButton, helpButton);
        buttons.setAlignment(Pos.TOP_CENTER);
        buttons.setMinHeight(buttonHeight);
        buttons.setSpacing(gap);
        buttons.setPadding(new Insets(gap));

        VBox clues = new VBox(gap);
        clues.setPrefWidth(clueWidth);
        clues.setPadding(new Insets(gap));
        clues.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));

        Paint clueActive = Color.BLACK;
        Paint clueInactive = Color.LIGHTGRAY;

        for (String clue : puzzle.getClues()) {
            Label label = new Label(clue);
            label.setWrapText(true);
            label.setTextFill(clueActive);
            label.setCursor(Cursor.HAND);
            label.setTooltip(new Tooltip("Click to mark clues as used/unused."));
            clues.getChildren().add(label);

            label.setOnMouseClicked(e -> {
                if (e.getButton() != MouseButton.PRIMARY) {
                    return;
                }

                if (label.getTextFill() == clueInactive) {
                    label.setTextFill(clueActive);
                } else {
                    label.setTextFill(clueInactive);
                }
            });
        }

        BorderPane root = new BorderPane(gui.getPane(), buttons, clues, null, null);

        primaryStage.setScene(new Scene(root, width, height));
        primaryStage.setResizable(false);
        startTime = System.currentTimeMillis();
        primaryStage.show();
    }

    private void onHelp(ActionEvent actionEvent) {
        final String help =
                "Use the clues on the left to determine the correct combinations.\n" +
                "Properties are listed on the top and left.  Use the resulting grid to keep track of the clues.\n" +
                "Click a grid cell to rotate between unknown, correct, and incorrect.\n" +
                "Click \"Clean\" to clear any incorrect markings on the grid.\n" +
                "Click \"Hint\" to reveal one correct answer that you haven't yet marked.\n" +
                "When you're done, click \"Check\" to see if you're correct.\n" +
                "Once you've successfully completed and checked the puzzle, your scores will lock and won't be affected by further changes you make to the grid.";
        Alert alert = new Alert(Alert.AlertType.INFORMATION, help, ButtonType.OK);
        alert.initModality(Modality.NONE);
        alert.show();
    }

    private void onCheck(ActionEvent event) {
        boolean correct = gui.isCorrect();

        if (endTime == 0) {
            attempts++;
        }

        Alert alert;
        if (correct || endTime != 0) {
            if (endTime == 0) {
                endTime = System.currentTimeMillis();
            }

            alert = new Alert(Alert.AlertType.INFORMATION, String.format(
                    "You've completed the puzzle successfully.\n\nAttempts: %d\nTime: %d seconds\nHints: %d\nCleaned mistakes: %d",
                    attempts,
                    (endTime - startTime) / 1000,
                    hints,
                    cleanedMistakes
            ), ButtonType.FINISH);
        } else {
            alert = new Alert(Alert.AlertType.ERROR, "The puzzle isn't finished yet: guesses are missing or incorrect.", ButtonType.OK);
        }

        alert.initModality(Modality.WINDOW_MODAL);
        alert.show();
    }

    private void onClean(ActionEvent event) {
        int mistakes = gui.clean();

        if (endTime == 0) {
            cleanedMistakes += mistakes;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION, String.format("The puzzle has been cleared of %d incorrect guess(es).", mistakes), ButtonType.OK);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.show();
    }

    private void onHint(ActionEvent event) {
        if (endTime == 0) {
            hints++;
        }

        gui.hint();
    }
}