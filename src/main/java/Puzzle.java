import java.util.*;

public class Puzzle {
    private final List<Property> properties;
    private final List<Permutation> answers;

    public Puzzle() {
        // Example taken from http://logic-puzzles.org/game.php?u2=73221166b3f321eed9e3e2f20123b220
        this(
                new String[] {
                        "Countries",
                        "Months",
                        "Years",
                },
                new String[][] {
                        new String[] { "Brunei",         "Iraq",           "New Zealand",    "Rwanda"         },
                        new String[] { "February",       "April",          "June",           "October"        },
                        new String[] { "210,000 B.C.E.", "180,000 B.C.E.", "150,000 B.C.E.", "120,000 B.C.E." },
                },
                new int[][] {
                        new int[] { 3, 0, 0 },
                        new int[] { 0, 1, 1 },
                        new int[] { 2, 3, 2 },
                        new int[] { 1, 2, 3 },
                }
        );
    }

    public Puzzle(String[] aProperties, String[][] aPropertyValues, int[][] aAnswers) {
        assert aProperties.length > 0;
        assert aProperties.length == aPropertyValues.length;
        assert aProperties.length == aAnswers[0].length;
        assert aPropertyValues.length == aAnswers.length;

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
}
