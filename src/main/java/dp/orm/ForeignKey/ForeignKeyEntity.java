package dp.orm.ForeignKey;

public class ForeignKeyEntity {
    private String name;
    private String referencedClass;
    private String referencedField;

    public ForeignKeyEntity(String name, String referencedClass, String referencedField) {
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
