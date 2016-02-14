import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class Permutation {
    private final Map<Property, PropertyValue> values = new HashMap<>();

    public Permutation set(Property property, PropertyValue value) {
        values.put(property, value);
        return this;
    }

    public Map<Property, PropertyValue> getValues() {
        return Collections.unmodifiableMap(values);
    }

    public PropertyValue get(Property property) {
        return values.get(property);
    }
}
