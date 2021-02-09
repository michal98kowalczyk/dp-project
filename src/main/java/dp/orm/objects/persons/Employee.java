package dp.orm.objects.persons;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import lombok.Data;

@Data
@DatabaseTable
public class Employee extends Person {


    @DatabaseField
    public String company;
}
