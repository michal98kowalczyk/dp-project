package dp.orm.mapping;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class InheritanceMapping {
    private Map<String, String> fieldToTable;

    public InheritanceMapping(Map<String, String> fieldToTable) {
        this.fieldToTable = fieldToTable;
    }

    public Set<String> getAllTableSchema() {
        return fieldToTable.entrySet().stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
    }

    public String getTableSchema(String field) {
        return fieldToTable.get(field);
    }

    public void union(Map<String, String> mapping) {
        fieldToTable.putAll(mapping);
    }


}
