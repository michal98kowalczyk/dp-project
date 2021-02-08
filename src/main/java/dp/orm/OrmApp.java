package dp.orm;



import dp.orm.objects.Animal;
import dp.orm.objects.Cat;
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
        dog.setId(0);
        dog.setName("first");
        dog.setType("dog");
        dog.setOwner("michal");

        Dao<Dog> daoDog = OrmManager.getDao(Dog.class);

        daoDog.insert(dog);

        Cat cat = new Cat();
        cat.setId(1);
        cat.setName("kot");
        cat.setType("cat");
        cat.setOwner("michal");

        Dao<Cat> daoCat = OrmManager.getDao(Cat.class);

        daoCat.insert(cat);

//        System.out.println(dog.toString());

//        System.out.println(NameUtils.extractTableName(Dog.class));
//        List<Field> fields = FieldUtils.getAllFields(Dog.class);
//
//        fields.forEach(field -> System.out.println(field));
//        System.out.println(NameUtils.extractColumnName(fields.get(0)));
    }
}
