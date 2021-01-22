package dp.orm.mapper;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.Id;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class InheritanceMapper {



    public abstract <T> InheritanceMapping map(Class<T> cls);

    public ColumnSchema getId(List<Field> fields){

        List<Field>  idArray = fields.stream()
                .filter(field -> field.isAnnotationPresent(Id.class))
                .collect(Collectors.toList());

        if (idArray.size() > 1){
            throw new RuntimeException();
        }else if (idArray.isEmpty()){
            throw new RuntimeException();
        }

        return new ColumnSchema(idArray.get(0));

    }



    public Set<ColumnSchema> createColumns(List<Field> fields){

        return fields.stream().filter(field -> field.isAnnotationPresent(DatabaseField.class))
                .map(ColumnSchema::new).collect(Collectors.toSet());
    }

}
