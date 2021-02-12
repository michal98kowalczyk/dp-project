package dp.orm.executors;

import dp.orm.exceptions.InsertException;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.query.InsertQuery;
import dp.orm.query.QueryBuilder;
import dp.orm.query.QueryDirector;
import lombok.extern.apachecommons.CommonsLog;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@CommonsLog
public class InsertExecutor {

    private DataSource dataSource;
    private InheritanceMapping inheritanceMapping;

    private InsertExecutor(Builder builder) {
        dataSource = builder.dataSource;
        inheritanceMapping = builder.inheritanceMapping;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DataSource dataSource;
        private InheritanceMapping inheritanceMapping;

        private Builder() {
        }

        public Builder dataSource(DataSource dataSource) {
            this.dataSource = dataSource;
            return this;
        }

        public Builder inheritanceMapping(InheritanceMapping inheritanceMapping) {
            this.inheritanceMapping = inheritanceMapping;
            return this;
        }

        public InsertExecutor build() {
            return new InsertExecutor(this);
        }
    }


    public <T> void execute(T object){

        log.info("Insert object " + object.getClass().getName());

        QueryBuilder queryBuilder = new InsertQuery(inheritanceMapping);

        QueryDirector<T> queryDirector = new QueryDirector<>(queryBuilder);


        String query;

        try {
            query = queryDirector.withObject(object).build();
        } catch (InvocationTargetException e) {
            throw new InsertException("Error during insertion "+object.toString(),e);
        } catch (IllegalAccessException e) {
            throw new InsertException("Error during insertion "+object.toString(),e);
        }


        try(Connection connection = dataSource.getConnection()) {
            System.out.println(query);

            Statement statement = connection.createStatement();

            statement.execute(query);


            log.info("Insertion done "+object.getClass().toString());
        } catch (SQLException e) {
            throw new InsertException("Error during insertion "+object.toString(),e);
        }
    }

}
