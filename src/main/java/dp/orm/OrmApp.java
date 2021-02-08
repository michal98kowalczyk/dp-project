package dp.orm;



import dp.orm.objects.Animal;
import dp.orm.objects.Dog;
import dp.orm.utlis.FieldUtils;
import dp.orm.utlis.NameUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.lang.reflect.Field;
import java.util.List;

@SpringBootApplication
public class OrmApp {
    public static void main(String[] args){

        SpringApplication.run(OrmApp.class,args);

        Dog dog = new Dog();
        dog.setName("first");
        dog.setType("dog");
        dog.setOwner("michal");
        System.out.println(dog.toString());

        System.out.println(NameUtils.extractTableName(Dog.class));
        List<Field> fields = FieldUtils.getAllFields(Dog.class);

        fields.forEach(field -> System.out.println(field));
        System.out.println(NameUtils.extractColumnName(fields.get(0)));
    }
}
