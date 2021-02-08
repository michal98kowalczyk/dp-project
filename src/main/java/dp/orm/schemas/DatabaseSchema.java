package dp.orm.schemas;


import dp.orm.mapping.InheritanceMapping;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class DatabaseSchema {

    private Map<Class<?>, InheritanceMapping>  classToTable = new HashMap<>();

    private Set<TableSchema> tables = new HashSet<>();

    public InheritanceMapping getMapping(Class<?> cls){
        return classToTable.get(cls);
    }

    public InheritanceMapping addMapping(Class<?> cls, InheritanceMapping mapping){
        this.classToTable.put(cls,mapping);
        return mapping;
    }

    public Set<TableSchema> getTables(){
        return tables;
    }


    public void addTable(TableSchema table){
        this.tables.add(table);
    }


    public void addTables(Collection<TableSchema> tables){
        this.tables.addAll(tables);
    }


}
