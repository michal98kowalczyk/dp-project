package dp.orm.executors;


import dp.orm.exceptions.CreateException;
import dp.orm.query.CreateQuery;
import dp.orm.query.QueryBuilder;
import dp.orm.query.QueryDirector;
import dp.orm.schemas.TableSchema;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
@CommonsLog
public class CreateExecutor {

    @Autowired
    private DataSource dataSource;

    public <T> void createTable(TableSchema tableSchema){

        log.info("Create table for " + tableSchema.getName());

        QueryBuilder queryBuilder = new CreateQuery();
        QueryDirector<T> queryDirector = new QueryDirector<>(queryBuilder);
        String query;

        try {
            query = queryDirector.withObject((T)tableSchema).build();
        } catch (InvocationTargetException e) {
            throw new CreateException("Error during create table " +tableSchema.getName(),e);
        } catch (IllegalAccessException e) {
            throw new CreateException("Error during create table " +tableSchema.getName(),e);
        }


        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            System.out.println(query);
            statement.executeUpdate(query);

            log.info("Table " + tableSchema.getName() + " created!");


        }catch (SQLException e){

            throw new CreateException("Error during create table " +tableSchema.getName(),e);
        }

    }
}
