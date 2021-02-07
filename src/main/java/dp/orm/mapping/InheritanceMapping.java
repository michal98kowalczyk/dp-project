package dp.orm.mapping;

import dp.orm.schemas.TableSchema;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InheritanceMapping {
    private Map<String, TableSchema> fieldToTable;

    public InheritanceMapping(Map<String, TableSchema> fieldToTable) {
        this.fieldToTable = fieldToTable;
    }

    public Set<TableSchema> getAllTableSchema() {
        return this.fieldToTable.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());

    }

    public TableSchema getTableSchema(String field) {
        return this.fieldToTable.get(field);
    }

    public void union(Map<String, TableSchema> mapping) {
        this.fieldToTable.putAll(mapping);
    }


}
