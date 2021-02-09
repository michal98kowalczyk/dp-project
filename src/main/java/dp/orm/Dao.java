package dp.orm;

import dp.orm.assemblers.ObjectAssembler;
import dp.orm.executors.DeleteExecutor;
import dp.orm.executors.InsertExecutor;
import dp.orm.executors.SelectExecutor;
import dp.orm.executors.UpdateExecutor;
import dp.orm.mapping.InheritanceMapping;

import javax.sql.DataSource;
import java.util.List;

public class Dao<T> {

    private Class<T> cls;
    private InsertExecutor insertExecutor;
    private DeleteExecutor deleteExecutor;
    private SelectExecutor selectExecutor;
    private UpdateExecutor updateExecutor;

    public Dao(DataSource dataSource, Class<T> cls, InheritanceMapping inheritanceMapping) {
        this.cls = cls;
        this.insertExecutor = InsertExecutor.builder()
                .dataSource(dataSource)
                .inheritanceMapping(inheritanceMapping)
                .build();
        this.deleteExecutor = DeleteExecutor.builder()
                .dataSource(dataSource)
                .inheritanceMapping(inheritanceMapping)
                .build();
        this.selectExecutor = SelectExecutor.builder()
                .dataSource(dataSource)
                .inheritanceMapping(inheritanceMapping)
                .objectAssembler(new ObjectAssembler<>())
                .build();
        this.updateExecutor = UpdateExecutor.builder()
                .dataSource(dataSource)
                .inheritanceMapping(inheritanceMapping)
                .build();
    }

    public void insert(T obj) {
        insertExecutor.execute(obj);
    }

    public void delete(int id) {
        deleteExecutor.execute(id, this.cls);
    }

    public void update(T object) {
        updateExecutor.execute(object);
    }

    public List<T> findAll() {
        return selectExecutor.findAll(cls);
    }

    public T findById(int id) {
        return selectExecutor.findById(id, cls);
    }
}
