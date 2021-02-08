package dp.orm.mapping;

import dp.orm.schemas.TableSchema;

import java.util.Map;

public class ClassInheritanceMapping extends InheritanceMapping {
    private InheritanceMapping parent;

    public ClassInheritanceMapping(Map<String, TableSchema> fieldToTable, InheritanceMapping parentMapping) {
        super(fieldToTable);
        this.parent = parentMapping;
    }

    @Override
    public TableSchema getTableSchema(String field) {
        var tableSchema = super.getTableSchema(field);
        if ( tableSchema == null ) {
            tableSchema = parent.getTableSchema(field);
        }
        return tableSchema;
    }

}