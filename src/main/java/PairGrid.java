import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.List;

public final class PairGrid {
    private static final Paint BORDER_COLOR = Color.BLACK;

    private final Property topProperty;
    private final Property leftProperty;
    private final int size;
    private final GridCell[][] cells;
    private final GridPane pane;
    private final double pixels;

    public PairGrid(double pixels, double border, Property topProperty, Property leftProperty, List<Permutation> answers) {
        this.pixels = pixels;
        this.size = topProperty.getValues().size();
        this.topProperty = topProperty;
        this.leftProperty = leftProperty;
        this.cells = new GridCell[size][size];
        this.pane = new GridPane();

        pane.setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(border))));
        pane.setBackground(new Background(new BackgroundFill(BORDER_COLOR, null, null)));
        pane.setHgap(1);
        pane.setVgap(1);

        double totalPixels = (pixels + 1) * size  - 1 + border * 2;
        pane.setPrefWidth(totalPixels);
        pane.setPrefHeight(totalPixels);
        pane.setMaxWidth(totalPixels);
        pane.setMaxHeight(totalPixels);
        pane.setMinWidth(totalPixels);
        pane.setMinHeight(totalPixels);

        for (int top = 0; top < size; top++) {
            pane.getColumnConstraints().add(new ColumnConstraints(pixels, pixels, pixels, Priority.NEVER, HPos.CENTER, false));

            for (int left = 0; left < size; left++) {
                pane.getRowConstraints().add(new RowConstraints(pixels, pixels, pixels, Priority.NEVER, VPos.CENTER, false));

                cells[top][left] = new GridCell(top, left);
            }
        }

        for (Permutation answer : answers) {
            PropertyValue top = answer.get(topProperty);
            PropertyValue left = answer.get(leftProperty);

            GridCell cell = cells[top.getValueId()][left.getValueId()];
            cell.setAnswer(true);
        }
    }

    public GridCell getCell(int top, int left) {
        return cells[top][left];
    }

    public Property getTopProperty() {
        return topProperty;
    }

    public Property getLeftProperty() {
        return leftProperty;
    }

    public boolean isCorrect() {
        for (GridCell[] row : cells) {
            for (GridCell cell : row) {
                if (!cell.isCorrect()) {
                    return false;
                }
            }
        }

        return true;
    }

    public int clean() {
        int mistakes = 0;

        for (GridCell[] row : cells) {
            for (GridCell cell : row) {
                mistakes += cell.clean();
            }
        }

        return mistakes;
    }

    public GridPane getPane() {
        return pane;
    }

    public void hint() {
        for (GridCell[] row : cells) {
            for (GridCell cell : row) {
                if (cell.isAnswer() && !cell.isCorrect()) {
                    cell.hint();
                    return;
                }
            }
        }
    }

    public class GridCell implements EventHandler<ActionEvent> {
        private State currentState;
        private State explicitState;
        private boolean answer;
        private final Button button;
        private final int top;
        private final int left;
        private int implicitlyIncorrectCount = 0;

        public GridCell(int top, int left) {
            this.top = top;
            this.left = left;
            this.button = new Button();

            pane.add(button, top, left);
            button.prefHeightProperty().bindBidirectional(button.prefWidthProperty());
            button.setPrefWidth(pixels);
            button.setOnAction(this);
            button.setCursor(Cursor.HAND);

            button.setTooltip(new Tooltip(String.format(
                    "%s = %s",
                    getTopProperty().getValue(top).getValue(),
                    getLeftProperty().getValue(left).getValue()
            )));

            setCurrentState(State.UNKNOWN, true);
        }

        public void setAnswer(boolean value) {
            answer = value;
        }

        public boolean isAnswer() {
            return answer;
        }

        public boolean isCorrect() {
            switch (getCurrentState()) {
                case CORRECT:
                    return isAnswer();

                case INCORRECT:
                case IMPLICITLY_INCORRECT:
                case UNKNOWN:
                    return !isAnswer();

                default:
                    return false;
            }
        }

        public int clean() {
            int mistakes = 0;

            if (isCorrect() || currentState == State.UNKNOWN) {
                return mistakes;
            }

            if (currentState != State.IMPLICITLY_INCORRECT) {
                mistakes = 1;
            }

            setCurrentState(State.UNKNOWN, true);
            return mistakes;
        }

        public State getCurrentState() {
            return currentState;
        }

        private void implyIncorrect(GridCell impliedBy) {
            if (impliedBy == this) {
                return;
            }

            implicitlyIncorrectCount++;

            if (currentState == State.UNKNOWN) {
                explicitState = currentState;
                setCurrentState(State.IMPLICITLY_INCORRECT, false);
            }
        }

        private void unimplyIncorrect(GridCell impliedBy) {
            if (impliedBy == this) {
                return;
            }

            if (--implicitlyIncorrectCount <= 0) {
                implicitlyIncorrectCount = 0;
                setCurrentState(explicitState, true);
            }
        }

        @SuppressWarnings("SuspiciousNameCombination")
        public void setCurrentState(State value, boolean explicit) {
            if (currentState == value) {
                return;
            }

            if (explicit) {
                if (value == State.CORRECT) {
                    for (int x = 0; x < size; x++) {
                        getCell(x, left).implyIncorrect(this);
                    }
                    for (int y = 0; y < size; y++) {
                        getCell(top, y).implyIncorrect(this);
                    }
                } else if (currentState == State.CORRECT) {
                    for (int x = 0; x < size; x++) {
                        getCell(x, left).unimplyIncorrect(this);
                    }
                    for (int y = 0; y < size; y++) {
                        getCell(top, y).unimplyIncorrect(this);
                    }
                }
            }

            if (explicit) {
                explicitState = value;
            }

            if (explicit && value == State.UNKNOWN && implicitlyIncorrectCount > 0) {
                currentState = State.IMPLICITLY_INCORRECT;
            } else {
                currentState = value;
            }

            update();
        }

        private void update() {
            Color bg;
            Color fg;
            String text;
            switch (getCurrentState()) {
                case CORRECT:
                    bg = new Color(0, 1, 0, 1);
                    fg = Color.BLACK;
                    text = ":)";
                    break;

                case INCORRECT:
                    bg = new Color(1, 0, 0, 1);
                    fg = Color.YELLOW;
                    text = "X";
                    break;

                case IMPLICITLY_INCORRECT:
                    bg = new Color(1, .5, .5, 1);
                    fg = Color.YELLOW;
                    text = "X";
                    break;

                case UNKNOWN:
                    bg = new Color(1, 1, 1, 1);
                    fg = Color.GRAY;
                    text = "?";
                    break;

                default:
                    bg = null;
                    fg = Color.RED;
                    text = "ERR";
                    break;
            }

            button.setBackground(new Background(new BackgroundFill(bg, null, null)));
            button.setBorder(Border.EMPTY);
            button.setPadding(Insets.EMPTY);
            button.setTextFill(fg);
            button.setText(text);
        }

        @Override
        public void handle(ActionEvent event) {
            setCurrentState(getCurrentState().next(), true);
        }

        public void hint() {
            setCurrentState(isAnswer() ? State.CORRECT : State.INCORRECT, true);
        }
    }
}
