package dp.orm.executors;

import dp.orm.assemblers.ObjectAssembler;
import dp.orm.exceptions.SelectException;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.query.QueryBuilder;
import dp.orm.query.QueryDirector;
import dp.orm.query.SelectQuery;
import lombok.Builder;
import lombok.extern.apachecommons.CommonsLog;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@CommonsLog
@Builder
public class SelectExecutor {

    private DataSource dataSource;
    private InheritanceMapping inheritanceMapping;
    private ObjectAssembler objectAssembler;


    public <T> T findById(int id, Class<T> clazz) {
        QueryBuilder builder = new SelectQuery(inheritanceMapping);
        QueryDirector<T> queryBuildDirector = new QueryDirector<>(builder);
        String query;
        try {
            query = queryBuildDirector.withObject((T) clazz).withCondition(id)
                    .build();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new SelectException(String.format("Could not select object with id %d class %s", id, clazz.getName()), e);
        }

        try (Connection conn = dataSource.getConnection()) {

            Statement stmt = conn.createStatement();

            log.info("Wykonano select findById(" + id + ")");
            ResultSet resultSet = stmt.executeQuery(query);
            resultSet.next();
            return (T) objectAssembler.assemble(clazz, resultSet);

        } catch (SQLException e) {
            log.error("Nie udalo sie wykonać selecta findById(" + id + ")");
            throw new RuntimeException(e);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> List<T> findAll(Class<T> clazz) {
        QueryBuilder builder = new SelectQuery(inheritanceMapping);
        QueryDirector<T> queryBuildDirector = new QueryDirector<>(builder);
        String query;

        try {
            query = queryBuildDirector.withObject((T) clazz)
                    .build();
        } catch (InvocationTargetException | IllegalAccessException e) {
            throw new SelectException(String.format("Could not select all objects class %s", clazz.getName()), e);
        }

        try (Connection conn = dataSource.getConnection()) {
            Statement stmt = conn.createStatement();
            log.info("Executing select findAll with query: " + query);
            ResultSet resultSet = stmt.executeQuery(query);
            return objectAssembler.bulkAssemble(clazz, resultSet);
        } catch (SQLException e) {
            log.error("Failed to execute query findAll");
            throw new RuntimeException(e);
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

}