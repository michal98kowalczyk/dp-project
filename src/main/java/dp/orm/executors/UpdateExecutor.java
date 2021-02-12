package dp.orm.executors;



import dp.orm.exceptions.UpdateException;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.query.QueryBuilder;
import dp.orm.query.QueryDirector;
import dp.orm.query.UpdateQuery;
import lombok.Builder;
import lombok.extern.apachecommons.CommonsLog;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@CommonsLog
@Builder
public class UpdateExecutor {

    private DataSource dataSource;
    private InheritanceMapping inheritanceMapping;


    public <T> ResultSet execute(T object) {

        QueryBuilder queryBuilder = new UpdateQuery(inheritanceMapping);
        QueryDirector<T> queryDirector = new QueryDirector<>(queryBuilder);
        String query;


        try {
            query = queryDirector.withObject(object).build();
        } catch (IllegalAccessException e) {
            throw new UpdateException("Error during update "+object.toString(),e);
        } catch (InvocationTargetException e) {
            throw new UpdateException("Error during update "+object.toString(),e);
        }


        try (Connection connection = dataSource.getConnection()) {
            System.out.println(query);
            log.info("Update " + object.getClass().getName());

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);
            return statement.getResultSet();

        } catch (SQLException throwables) {
            throw new UpdateException("Error during update "+object.toString(),throwables);
        }


    }

}
