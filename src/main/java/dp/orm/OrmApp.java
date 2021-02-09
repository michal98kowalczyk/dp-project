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

        Dog dog = new Dog();
        dog.setId(0);
        dog.setDogId(0);
        dog.setName("first");
        dog.setType("dog");
        dog.setOwner("michal");

        Dao<Dog> daoDog = OrmManager.getDao(Dog.class);

        daoDog.insert(dog);

        Cat cat = new Cat();
        cat.setId(1);
        cat.setCatId(1);
        cat.setName("kot");
        cat.setType("cat");
        cat.setOwner("michal");

        Cat cat2 = new Cat();
        cat2.setId(2);
        cat2.setCatId(2);
        cat2.setName("kot2");
        cat2.setType("cat2");
        cat2.setOwner("michal");

        Dao<Cat> daoCat = OrmManager.getDao(Cat.class);

        daoCat.insert(cat);
        daoCat.insert(cat2);
        daoCat.delete(1);
        daoCat.findAll().forEach(System.out::println);


//        //        concrete table ----------------------

        Footballer footballer = new Footballer();
        footballer.setId(0);
        footballer.setClub("bayern");
        footballer.setName("robercik");
        Dao<Footballer> daoFoot = OrmManager.getDao(Footballer.class);

        daoFoot.insert(footballer);

        Cricketer cricketer = new Cricketer();
        cricketer.setId(0);
        cricketer.setBattingAverage((float) 2.0);
        cricketer.setName("cricket");
        Dao<Cricketer> daoCri = OrmManager.getDao(Cricketer.class);

        daoCri.insert(cricketer);

        Bowler bowler = new Bowler();
        bowler.setId(0);
        bowler.setName("bowl");
        bowler.setBowlingAverage(2.2);
        bowler.setBattingAverage((float) 1.0);
        Dao<Bowler> daoBow = OrmManager.getDao(Bowler.class);

        daoBow.insert(bowler);


        //        single table ----------------------


        Employee emp = new Employee();
        emp.setId(0);
        emp.setName("empl");
        emp.setCompany("comp");
        Dao<Employee> daoEmp = OrmManager.getDao(Employee.class);
        daoEmp.insert(emp);


        Employee emp2 = new Employee();
        emp2.setId(1);
        emp2.setName("empl2");
        emp2.setCompany("comp2");

        daoEmp.insert(emp2);

        daoEmp.delete(0);


//        System.out.println(dog.toString());

//        System.out.println(NameUtils.extractTableName(Dog.class));
//        List<Field> fields = FieldUtils.getAllFields(Dog.class);
//
//        fields.forEach(field -> System.out.println(field));
//        System.out.println(NameUtils.extractColumnName(fields.get(0)));
    }
}
