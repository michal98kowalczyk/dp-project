package dp.orm.ForeignKey;

public class ForeignKey {
    private String name;
    private String referencedClass;
    private String referencedField;

    public ForeignKey(String name, String referencedClass, String referencedField) {
        this.name = name;
        this.referencedClass = referencedClass;
        this.referencedField = referencedField;
    }

    public String getName() {
        return name;
    }

    public String getReferencedClass() {
        return referencedClass;
    }

    public String getReferencedField() {
        return referencedField;
    }
}
