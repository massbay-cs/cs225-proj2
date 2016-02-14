import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;

import java.util.LinkedList;
import java.util.List;

public final class MasterGrid {
    public static final Paint BORDER_COLOR = Color.BLACK;
    public static final double PIXELS = 30;
    public static final double GAP = 0;
    public static final double BORDER = 2;

    private final GridPane pane = new GridPane();
    private final List<PairGrid> subgrids = new LinkedList<>();
    private final double width;
    private final double height;

    @SuppressWarnings("SuspiciousNameCombination")
    public MasterGrid(Puzzle puzzle) {
        List<Property> properties = puzzle.getProperties();
        List<Permutation> answers = puzzle.getAnswers();

        int size = properties.size();
        double basePixels = (PIXELS + 1) * properties.get(0).getValues().size();
        double totalPixels = basePixels - 1 + BORDER * 2;
        double baseLabelPixels = basePixels * 2 / 3;
        double labelPixels = baseLabelPixels - 1 + BORDER * 2;
        this.width = (totalPixels + GAP + 2) * (properties.size() - 1) - GAP + labelPixels - 10 + PIXELS;
        this.height = this.width;

        pane.vgapProperty().bindBidirectional(pane.hgapProperty());
        pane.setHgap(GAP);
        pane.getColumnConstraints().add(0, new ColumnConstraints(PIXELS));
        pane.getRowConstraints().add(0, new RowConstraints(PIXELS));
        pane.getColumnConstraints().add(1, new ColumnConstraints(labelPixels));
        pane.getRowConstraints().add(1, new RowConstraints(labelPixels));

        for (int top = 0, column = 2; top < size - 1; top++, column++) {
            Property topProperty = properties.get(top);

            pane.getColumnConstraints().add(column, new ColumnConstraints(totalPixels));

            for (int left = size - 1, row = 2; left >= 1 && column <= size - row + 2; left--, row++) {
                if (top == left) {
                    continue;
                }

                Property leftProperty = properties.get(left);

                PairGrid pair = new PairGrid(PIXELS, BORDER, topProperty, leftProperty, answers);
                subgrids.add(pair);
                pane.getRowConstraints().add(row, new RowConstraints(totalPixels));
                pane.add(pair.getPane(), column, row);

                if (column == 2) {
                    {
                        Text text = new Text(leftProperty.getTitle());
                        text.setFill(Color.WHITE);
                        HBox box = new HBox(text);
                        box.setAlignment(Pos.CENTER);
                        box.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
                        box.setMaxWidth(totalPixels);
                        box.setMinWidth(totalPixels);
                        box.setMaxHeight(PIXELS);
                        double pivot = (totalPixels + PIXELS) / 4;
                        box.getTransforms().add(new Rotate(-90, pivot, pivot));
                        pane.add(box, 0, row);
                    }
                    {
                        GridPane labels = new GridPane();
                        labels.setMinWidth(labelPixels);
                        labels.setMinHeight(totalPixels);
                        labels.setPrefWidth(labelPixels);
                        labels.setPrefHeight(totalPixels);
                        labels.setMaxWidth(labelPixels);
                        labels.setMaxHeight(totalPixels);
                        labels.getColumnConstraints().add(new ColumnConstraints(labelPixels));
                        labels.setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(BORDER))));
                        labels.setBackground(new Background(new BackgroundFill(BORDER_COLOR, null, null)));
                        labels.setHgap(1);
                        labels.setVgap(1);
                        pane.add(labels, 1, row);
                        for (PropertyValue value : leftProperty.getValues()) {
                            Text text = new Text(value.getValue());
                            text.setTextAlignment(TextAlignment.RIGHT);
                            HBox box = new HBox(text);
                            box.setMaxWidth(baseLabelPixels);
                            box.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                            box.setPadding(new Insets(0, 5, 0, 5));
                            box.setAlignment(Pos.CENTER);
                            labels.getRowConstraints().add(new RowConstraints(PIXELS));
                            labels.add(box, 0, value.getValueId());
                        }
                    }
                }
            }

            {
                Text text = new Text(topProperty.getTitle());
                text.setFill(Color.WHITE);
                HBox box = new HBox(text);
                box.setAlignment(Pos.CENTER);
                box.setBackground(new Background(new BackgroundFill(Color.BLACK, null, null)));
                box.setMaxWidth(totalPixels);
                pane.add(box, column, 0);
            }

            {
                GridPane labels = new GridPane();
                labels.setMinWidth(labelPixels);
                labels.setMinHeight(totalPixels);
                labels.setPrefWidth(labelPixels);
                labels.setPrefHeight(totalPixels);
                labels.setMaxWidth(labelPixels);
                labels.setMaxHeight(totalPixels);
                labels.getColumnConstraints().add(new ColumnConstraints(labelPixels));
                labels.setBorder(new Border(new BorderStroke(BORDER_COLOR, BorderStrokeStyle.SOLID, null, new BorderWidths(BORDER))));
                labels.setBackground(new Background(new BackgroundFill(BORDER_COLOR, null, null)));
                labels.setHgap(1);
                labels.setVgap(1);
                pane.add(labels, column, 1);
                for (PropertyValue value : topProperty.getValues()) {
                    Text text = new Text(value.getValue());
                    HBox box = new HBox(text);
                    box.setMaxWidth(baseLabelPixels);
                    box.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
                    box.setPadding(new Insets(0, 5, 0, 5));
                    box.setAlignment(Pos.CENTER_LEFT);
                    labels.getRowConstraints().add(new RowConstraints(PIXELS));
                    labels.add(box, 0, value.getValueId());
                }
                labels.prefWidthProperty().bind(labels.heightProperty());
                double pivot = (labelPixels + totalPixels) / 4;
                labels.getTransforms().add(new Rotate(-90, pivot, pivot));
            }
        }
    }

    public boolean isCorrect() {
        for (PairGrid pair : subgrids) {
            if (pair != null && !pair.isCorrect()) {
                return false;
            }
        }

        return true;
    }

    public int clean() {
        int mistakes = 0;

        for (PairGrid pair : subgrids) {
            mistakes += pair.clean();
        }

        return mistakes;
    }

    public GridPane getPane() {
        return pane;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public void hint() {
        for (PairGrid pair : subgrids) {
            if (!pair.isCorrect()) {
                pair.hint();
                return;
            }
        }
    }
}
