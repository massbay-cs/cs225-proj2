import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class Property {
    private final int id;
    private final String title;
    private final List<PropertyValue> values;

    private Property(String title) {
        this.id = -1;
        this.title = title;
        this.values = null;
    }

    public Property(int id, String title, List<PropertyValue> values) {
        this.id = id;
        this.title = title;
        this.values = Collections.unmodifiableList(new ArrayList<>(values));
    }

    public static Property placeholder(String title) {
        return new Property(title);
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

    public boolean isPlaceholder() {
        return id < 0 || values == null;
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass() && title.equals(((Property) obj).getTitle());

    }

    @Override
    public String toString() {
        return title;
    }
}
