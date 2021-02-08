package dp.orm;

import dp.orm.executors.InsertExecutor;
import dp.orm.mapping.InheritanceMapping;

import javax.sql.DataSource;

public class Dao<T> {

    private Class<T> cls;
    private InsertExecutor insertExecutor;

    public Dao(DataSource dataSource, Class<T> cls, InheritanceMapping inheritanceMapping){
        this.cls = cls;
        this.insertExecutor = InsertExecutor.builder().dataSource(dataSource).inheritanceMapping(inheritanceMapping).build();

    }

    public void insert(T obj){
        insertExecutor.execute(obj);
    }
}
