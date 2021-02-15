package dp.orm.schemas;


import dp.orm.JoinColumn.JoinColumnEntity;
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

    private boolean isJoinColumnAnnotationPresent = false;

    private JoinColumnEntity joinColumnEntity = null;

    public void addColumn(ColumnSchema column){
        columns.add(column);
    }

    public void addColumns(Set<ColumnSchema>  columnsToAdd){
//        columns.addAll(columns);
        columnsToAdd.forEach(columnSchema -> this.addColumn(columnSchema));

    }

    public void setJoinColumn(JoinColumnEntity joinColumnEntity) {
        this.joinColumnEntity = joinColumnEntity;
        this.isJoinColumnAnnotationPresent = true;
    }

    public boolean isJoinColumnAnnotationPresent() {
        return this.isJoinColumnAnnotationPresent;
    }

    public JoinColumnEntity getJoinColumnEntity() {
        return this.joinColumnEntity;
    }




}
