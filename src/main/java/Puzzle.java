import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Puzzle {
    private int propertyCount;
    private int propertyValueCount;
    private String[] properties;
    private String[][] propertyValues;
    private State[][][] guessMatrix;
    private State[][][] answerMatrix;

    public Puzzle() {
        // Example taken fromhttp://logic-puzzles.org/game.php?u2=73221166b3f321eed9e3e2f20123b220
        this(
                new String[] {
                        "countries",
                        "months",
                        "years",
                },
                new String[][] {
                        new String[] { "Brunei",         "Iraq",           "New Zealand",    "Rwanda"         },
                        new String[] { "February",       "April",          "June",           "October"        },
                        new String[] { "210,000 B.C.E.", "180,000 B.C.E.", "150,000 B.C.E.", "120,000 B.C.E." },
                },
                new String[][] {
                        new String[] { "Rwanda",      "February", "210,000 B.C.E." },
                        new String[] { "Brunei",      "April",    "180,000 B.C.E." },
                        new String[] { "New Zealand", "October",  "150,000 B.C.E." },
                        new String[] { "Iraq",        "June",     "120,000 B.C.E." },
                }
        );
    }

    public Puzzle(String[] properties, String[][] propertyValues, String[][] combinations) {
        this.properties = properties;
        this.propertyCount = properties.length;
        this.propertyValues = propertyValues;
        this.propertyValueCount = propertyValues.length;

        List<Map<String, Integer>> keys = new ArrayList<>(propertyCount);
        for (int propertyId = 0; propertyId < propertyCount; propertyId++) {
            Map<String, Integer> propertyKeys = new HashMap<>();
            keys.add(propertyKeys);

            for (int i = 0; i < propertyValueCount; i++) {
                propertyKeys.put(propertyValues[propertyId][i], i);
            }
        }
    }

    public void changeState(int x, int y, int z, State state) {

    }
}
