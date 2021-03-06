package dp.orm;

import dp.orm.annotations.DatabaseTable;
import dp.orm.executors.CreateExecutor;
import dp.orm.mapper.InheritanceMapper;
import dp.orm.mapping.InheritanceMapping;
import dp.orm.schemas.ColumnSchema;
import dp.orm.schemas.DatabaseSchema;
import dp.orm.schemas.TableSchema;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@CommonsLog
@Component
public class DatabaseCreator implements CommandLineRunner {

    @Autowired
    private DatabaseSchema databaseSchema;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private CreateExecutor createExecutor;

    private String packageWithObjects = "dp.orm";

    @Override
    public void run(String... args) throws Exception {
        List<Class<?>> classes = findClasses();

        classes.forEach(cls -> {
            InheritanceMapper inheritanceMapper = null;

            try {
                inheritanceMapper = cls.getAnnotation(DatabaseTable.class)
                        .inheritanceType().getMappingClass().getConstructor(DatabaseSchema.class).newInstance(databaseSchema);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }

            System.out.println("Znaleziozna klasa "+cls.getName());
            inheritanceMapper.map(cls);
        });


        System.out.println("Ile jest znalezionych klas: " + classes.size());
//        System.out.println("Ile jest znalezionych klas: " + classes.size());

        classes.forEach(cls -> {



            InheritanceMapping mapping = databaseSchema.getMapping(cls);
            databaseSchema.addTables(mapping.getAllTableSchema());


            System.out.println(cls.getName() + " jej mapping to "+ databaseSchema.getMapping(cls));

//            System.out.println("DB creator all table schema");
//            mapping.getAllTableSchema().forEach(f->System.out.println(f));

            OrmManager.addDao(cls,new Dao<>(dataSource,cls,mapping));
        });


        databaseSchema.getTables().forEach(System.out::println);

//        databaseSchema.getTables().forEach(tableSchema -> createExecutor.createTable(tableSchema));

        List<TableSchema> tablesWithForeignKeys = new ArrayList<>();

        for (TableSchema tableSchema : databaseSchema.getTables()) {


            for (ColumnSchema columnSchema : tableSchema.getColumns()) {
                if (columnSchema.isForeignKey()) {
                    tablesWithForeignKeys.add(tableSchema);

                }
            }
            if (tablesWithForeignKeys.contains(tableSchema)) continue;
            createExecutor.createTable(tableSchema);
        }

        tablesWithForeignKeys.forEach(tableSchema -> createExecutor.createTable(tableSchema));
    }

    private List<Class<?>> findClasses() {

        List<Class<?>> classes = new ArrayList<>();

        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AnnotationTypeFilter(DatabaseTable.class));


        for (BeanDefinition beanDefinition : provider.findCandidateComponents(packageWithObjects)){
            Class<?> cls = null;
            try {
                cls = Class.forName(beanDefinition.getBeanClassName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            classes.add(cls);
         }

        classes.forEach(System.out::println);

        return classes;
    }


}
