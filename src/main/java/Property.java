import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Property {
    private final int id;
    private final String title;
    private final List<PropertyValue> values;

    public Property(int id, String title, List<PropertyValue> values) {
        this.id = id;
        this.title = title;
        this.values = Collections.unmodifiableList(new ArrayList<>(values));
    }

    public int getId() {
        return id;
    }

    public List<PropertyValue> getValues() {
        return values;
    }

    public String getTitle() {
        return title;
    }

    public PropertyValue getValue(int id) {
        return values.get(id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass() && id == ((Property) obj).id && title.equals(((Property) obj).title);

    }

    @Override
    public String toString() {
        return title;
    }
}
