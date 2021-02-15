package dp.orm.JoinColumn;

public class JoinColumnEntity {
    private String name;
    private String referencedColumnName;
    private String referencedClass;

    public JoinColumnEntity(String name, String referencedColumnName, String referencedClass) {
        this.name = name;
        this.referencedColumnName = referencedColumnName;
        this.referencedClass = referencedClass;
    }

    public String getName() {
        return name;
    }

    public String getReferencedColumnName() {
        return referencedColumnName;
    }

    public String getReferencedClass() {
        return referencedClass;
    }
}
