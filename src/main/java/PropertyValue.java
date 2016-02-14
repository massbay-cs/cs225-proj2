import java.util.Objects;

public final class PropertyValue {
    private final int propertyId;
    private final int valueId;
    private final String value;

    public PropertyValue(int propertyId, int valueId, String value) {
        assert propertyId >= 0;
        assert value != null;

        this.propertyId = propertyId;
        this.valueId = valueId;
        this.value = value;
    }

    public int getPropertyId() {
        return propertyId;
    }

    public int getValueId() {
        return valueId;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || obj.getClass() != getClass()) {
            return false;
        }

        PropertyValue other = (PropertyValue) obj;
        return other.propertyId == propertyId && other.valueId == valueId && other.value.equals(value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(propertyId, valueId, value);
    }

    @Override
    public String toString() {
        return value;
    }
}
