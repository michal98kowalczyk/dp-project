package dp.orm;


import dp.orm.objects.Company.Address;
import dp.orm.objects.Company.Contact;
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
import org.checkerframework.checker.units.qual.C;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class OrmApp {
    public static void main(String[] args) {

        SpringApplication.run(OrmApp.class, args);

        //     class table ----------------------
//
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
//        daoCat.delete(1);
//        cat2.setName("kotttt2");
//        daoCat.update(cat2,2);
//
//
//////        //        concrete table ----------------------
//
//
//        Player player0 = new Player();
//        player0.setName("player0");
//        Dao<Player> daoPlayer = OrmManager.getDao(Player.class);
//        daoPlayer.insert(player0);
//
//        Footballer footballer = new Footballer();
//
//        footballer.setClub("bayern");
//        footballer.setName("robercik");
//        Dao<Footballer> daoFoot = OrmManager.getDao(Footballer.class);
//
//        daoFoot.insert(footballer);
//
//        Cricketer cricketer = new Cricketer();
//        cricketer.setBattingAverage((float) 2.0);
//        cricketer.setName("cricket");
//        Dao<Cricketer> daoCri = OrmManager.getDao(Cricketer.class);
//
//        daoCri.insert(cricketer);
//
//        Bowler bowler = new Bowler();
//        bowler.setName("bowl");
//        bowler.setBowlingAverage(2.2);
//        bowler.setBattingAverage((float) 1.2);
//        Dao<Bowler> daoBow = OrmManager.getDao(Bowler.class);
//
//        daoBow.insert(bowler);
//
//
//        //        single table ----------------------
//
//
//        Employee emp = new Employee();
//        emp.setName("empl");
//        emp.setCompany("comp");
//        Dao<Employee> daoEmp = OrmManager.getDao(Employee.class);
//        daoEmp.insert(emp);
//
//
//        Employee emp2 = new Employee();
//        emp2.setName("empl2");
//        emp2.setCompany("comp2");
//
//        daoEmp.insert(emp2);
//
//        daoEmp.delete(1);
//
//
//        Employee emp3 = new Employee();
//        emp3.setName("emp3");
//        emp3.setCompany("comp2");
//
//        daoEmp.insert(emp3);
//        emp3.setName("e3333");
//        daoEmp.update(emp3,3);
//
//        Dao<Animal> daoAnimal = OrmManager.getDao(Animal.class);
//        daoAnimal.findAll().forEach(System.out::println);
//
//        System.out.println(daoCat.findById(2));



//        --------relation


        Address address1 = new Address();
        address1.setStreet("street1");

        List<Contact> contacts = new ArrayList<>();
        Contact contact1 = new Contact();
        contact1.setContactName("contact1");
        Contact contact2 = new Contact();
        contact2.setContactName("contact2");

        contacts.add(contact1);
        contacts.add(contact2);

        Customer customer = new Customer();
        customer.setName("customer1");
        customer.setContacts(contacts);
        customer.setAddress(address1);

        Dao<Customer> customerDao = OrmManager.getDao(Customer.class);
        customerDao.insert(customer);

        customerDao.delete(1);

//        Dao<Address> addressDao = OrmManager.getDao(Address.class);
//        addressDao.delete(1);
//
//        Dao<Contact> contactDao = OrmManager.getDao(Contact.class);
//        contacts.forEach(contact -> contactDao.insert(contact));




    }
}
