import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Transform;

import java.util.LinkedList;
import java.util.List;

public final class MasterGrid {
    public static final double PIXELS = 30;

    private final GridPane pane = new GridPane();
    private final List<PairGrid> subgrids = new LinkedList<>();

    public MasterGrid(Puzzle puzzle) {
        pane.vgapProperty().bindBidirectional(pane.hgapProperty());
        pane.setHgap(3);

        List<Property> properties = puzzle.getProperties();
        List<Permutation> answers = puzzle.getAnswers();

        int size = properties.size();

        for (int top = 0, column = 1; top < size; top++, column++) {
            Property topProperty = properties.get(top);

            for (int left = size - 1, row = 1; left > 0; left--, row++) {
                Property leftProperty = properties.get(left);
                PairGrid pair = new PairGrid(PIXELS, topProperty, leftProperty, answers);

                subgrids.add(pair);
                pane.add(pair.getPane(), top + 1, row);

                if (top == 0) {
                    GridPane labels = new GridPane();
                    pane.add(labels, 0, row);
                    for (PropertyValue value : topProperty.getValues()) {
                        Text text = new Text(value.getValue());
                        labels.add(text, 0, value.getValueId());
                    }
                }
            }

            {
                GridPane labels = new GridPane();
                pane.add(labels, column, 0);
                for (PropertyValue value : topProperty.getValues()) {
                    Text text = new Text(value.getValue());
                    text.getTransforms().add(Transform.rotate(-90, 0, 0));
                    labels.add(text, value.getValueId(), 0);
                }
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

    public void clean() {
        for (PairGrid pair : subgrids) {
            if (pair != null) {
                pair.clean();
            }
        }
    }

    public GridPane getPane() {
        return pane;
    }
}
