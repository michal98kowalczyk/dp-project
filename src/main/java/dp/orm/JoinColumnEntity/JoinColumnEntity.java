package dp.orm.JoinColumnEntity;

public class JoinColumnEntity {
    private String name;
    private String referencedColumnName;

    public JoinColumnEntity(String name, String referencedColumnName) {
        this.name = name;
        this.referencedColumnName = referencedColumnName;
    }
}
