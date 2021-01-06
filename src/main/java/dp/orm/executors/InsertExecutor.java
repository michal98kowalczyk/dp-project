package dp.orm.executors;

import dp.orm.mapping.InheritanceMapping;
import lombok.extern.apachecommons.CommonsLog;

import javax.sql.DataSource;

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
}
