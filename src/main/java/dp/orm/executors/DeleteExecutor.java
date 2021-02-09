package dp.orm.executors;


import dp.orm.exceptions.DeleteException;
import dp.orm.exceptions.InsertException;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.query.DeleteQuery;
import dp.orm.query.InsertQuery;
import dp.orm.query.QueryBuilder;
import dp.orm.query.QueryDirector;
import lombok.Builder;
import lombok.extern.apachecommons.CommonsLog;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;


@CommonsLog
@Builder
public class DeleteExecutor {

    private DataSource dataSource;

    private InheritanceMapping inheritanceMapping;

    public <T> void execute(int id, Class cls){

        log.info("Delete object " + cls.getName());

        QueryBuilder queryBuilder = new DeleteQuery(inheritanceMapping);

        QueryDirector<T> queryDirector = new QueryDirector<>(queryBuilder);


        String query;

        try {
            query = queryDirector.withObject((T)cls).withCondition(id).build();
        } catch (InvocationTargetException e) {
            throw new InsertException("Error during delete the "+cls.toString(),e);
        } catch (IllegalAccessException e) {
            throw new InsertException("Error during delete the "+cls.toString(),e);
        }


        try(Connection connection = dataSource.getConnection()) {
            System.out.println("Query "+ query);

            Statement statement = connection.createStatement();

            statement.execute(query);


            log.info("Delete done "+cls.getName());
        } catch (SQLException e) {
            throw new DeleteException("Error during delete the "+cls.getName(),e);
        }
    }

}
