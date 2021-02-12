package dp.orm.schemas;


import lombok.*;

import java.util.Collection;
import java.util.Set;


//@ToString
//@RequiredArgsConstructor
//@EqualsAndHashCode
//@Setter
//@Getter
//@NoArgsConstructor
@Data
@Builder
public class TableSchema {

    private Class cls;

    private String name;

    private ColumnSchema id;

    private Set<ColumnSchema> columns;

    public void addColumn(ColumnSchema column){
        columns.add(column);
    }

    public void addColumns(Set<ColumnSchema>  columnsToAdd){
//        columns.addAll(columns);
        columnsToAdd.forEach(columnSchema -> this.addColumn(columnSchema));

    }




}
