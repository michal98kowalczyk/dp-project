package dp.orm;


import dp.orm.objects.Company.Customer;
import dp.orm.objects.animals.Animal;
import dp.orm.objects.animals.Cat;
import dp.orm.objects.animals.Dog;
//import dp.orm.objects.users.User;
import dp.orm.objects.persons.Employee;
import dp.orm.objects.players.Bowler;
import dp.orm.objects.players.Cricketer;
import dp.orm.objects.players.Footballer;
import dp.orm.objects.players.Player;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrmApp {
    public static void main(String[] args) {

        SpringApplication.run(OrmApp.class, args);

   //     class table ----------------------

//        Dog dog = new Dog();
//
//        dog.setName("first");
//        dog.setType("dog");
//        dog.setOwner("michal");
//
//        Dao<Dog> daoDog = OrmManager.getDao(Dog.class);
//
//        daoDog.insert(dog);
//
//        Cat cat = new Cat();
//
//        cat.setName("kot");
//        cat.setType("cat");
//        cat.setOwner("michal");
//
//        Cat cat2 = new Cat();
//        cat2.setName("kot2");
//        cat2.setType("cat2");
//        cat2.setOwner("michal");
//
//        Dao<Cat> daoCat = OrmManager.getDao(Cat.class);
//
//        daoCat.insert(cat);
//        daoCat.insert(cat2);
//        daoCat.delete(0);
//        cat2.setName("kotttt2");
//        daoCat.update(cat2,8);



    }
}
