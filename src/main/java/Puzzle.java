import java.util.*;

public class Puzzle {
    private final List<Property> properties;
    private final List<Permutation> answers;
    private final List<String> clues;

    public Puzzle() {
        this(new Random().nextInt());
    }

    public Puzzle(int puzzleId) {
        String[] aProperties;
        int[][] aAnswers;
        String[][] aPropertyValues;
        String[] clues;

        // Examples taken from http://logic-puzzles.org/
        final int mod = 3;
        final int normPuzzleId = Math.abs(puzzleId % mod);
        switch (normPuzzleId) {
            case 0:
            case 1:
            case 2:
                aProperties = new String[] { "diplomats", "durations", "capitals", "months" };
                aPropertyValues = new String[][] {
                        {"Anderson", "Jacobson", "Lee",   "Stevenson", "Underwood"},
                        {"2 day",    "3 day",    "4 day", "6 day",     "8 day"},
                        {"Brussels", "Dublin",   "Oslo",  "Paris",     "Vienna"},
                        {"January",  "February", "March", "April",     "May"},
                };
                /*
                 * 0:Anderson    0:2 day    2:Oslo         0:January
                 * 2:Lee         1:3 day    3:Paris        1:February
                 * 3:Stevenson   3:6 day    0:Brussels     2:March
                 * 1:Jacobson    4:8 day    4:Vienna       3:April
                 * 4:Underwood   2:4 day    1:Dublin       4:May
                 */
                aAnswers = new int[][]{
                        {0, 0, 2, 0},
                        {2, 1, 3, 1},
                        {3, 3, 0, 2},
                        {1, 4, 4, 3},
                        {4, 2, 1, 4},
                };
                clues = new String[] {
                        "Lee is either the diplomat going to Paris or the diplomat going to Vienna.",
                        "The diplomat leaving in February won't go to Oslo.",
                        "The ambassador with the 6 day visit will leave sometime before Jacobson.",
                        "The ambassador with the 2 day visit will leave 3 months before the person going to Vienna.",
                        "The diplomat leaving in February, Underwood, Jacobson and the ambassador with the 2 day visit are all different diplomats.",
                        "The ambassador with the 3 day visit will leave 1 month before the person going to Brussels.",
                        "Of the person going to Dublin and the ambassador with the 8 day visit, one is Mr. Underwood and the other will leave in April.",
                        "Stevenson won't leave for the 2 day visit.",
                };
                break;

            default:
                throw new RuntimeException(String.format("Assertion failure: %d %% %d = %d", puzzleId, mod, normPuzzleId));
        }

        assert aProperties.length > 0;
        assert aProperties.length == aPropertyValues.length;
        assert aProperties.length == aAnswers[0].length;
        assert aPropertyValues.length == aAnswers.length;

        this.clues = Collections.unmodifiableList(Arrays.asList(clues));

        List<Property> properties = new ArrayList<>(aProperties.length);
        for (int propertyId = 0; propertyId < aProperties.length; propertyId++) {
            String[] aValues = aPropertyValues[propertyId];

            List<PropertyValue> values = new ArrayList<>(aValues.length);
            for (int valueId = 0; valueId < aValues.length; valueId++) {
                values.add(valueId, new PropertyValue(propertyId, valueId, aValues[valueId]));
            }

            properties.add(propertyId, new Property(propertyId, aProperties[propertyId], values));
        }
        this.properties = Collections.unmodifiableList(properties);


        List<Permutation> answers = new ArrayList<>(aAnswers.length);
        for (int[] aAnswer : aAnswers) {
            assert aAnswer.length == properties.size();
            Permutation answer = new Permutation();

            for (int propertyId = 0; propertyId < aAnswer.length; propertyId++) {
                Property property = properties.get(propertyId);
                answer.set(properties.get(propertyId), property.getValue(aAnswer[propertyId]));
            }

            answers.add(answer);
        }
        this.answers = Collections.unmodifiableList(answers);
    }

    public List<Property> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public List<Permutation> getAnswers() {
        return Collections.unmodifiableList(answers);
    }

    public List<String> getClues() {
        return clues;
    }
}
