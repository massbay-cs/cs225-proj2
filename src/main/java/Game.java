import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Game extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Puzzle");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25,25,25,25));
        String gridBName = "";
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<10; j++) {
                gridBName = "Button" + i + j;
                Button  = new Button(gridBName);
                grid.add();
            }
        }

        Button StartOver = new Button("Start Over");
        Button Hint = new Button("Hint");
        HBox hbBtn = new HBox(10);
        hbBtn.setAlignment(Pos.BASELINE_CENTER);
        hbBtn.getChildren().add(Hint);
        hbBtn.getChildren().add(StartOver);
        grid.add(hbBtn,1,10);

        primaryStage.setScene(new Scene(grid, 300, 300));
        primaryStage.show();
    }
}