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

    public void addColumns(Collection<ColumnSchema> columns){
        columns.addAll(columns);
    }




}
