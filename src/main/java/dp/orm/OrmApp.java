package dp.orm;


import dp.orm.objects.animals.Cat;
import dp.orm.objects.animals.Dog;
//import dp.orm.objects.users.User;
import dp.orm.objects.persons.Employee;
import dp.orm.objects.players.Bowler;
import dp.orm.objects.players.Cricketer;
import dp.orm.objects.players.Footballer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrmApp {
    public static void main(String[] args) {

        SpringApplication.run(OrmApp.class, args);

//        class table ----------------------

        Dao<Dog> daoCat = OrmManager.getDao(Dog.class);
        daoCat.findAll().forEach(System.out::println);



//        System.out.println(dog.toString());

//        System.out.println(NameUtils.extractTableName(Dog.class));
//        List<Field> fields = FieldUtils.getAllFields(Dog.class);
//
//        fields.forEach(field -> System.out.println(field));
//        System.out.println(NameUtils.extractColumnName(fields.get(0)));
    }
}
