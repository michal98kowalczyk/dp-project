package dp.orm.objects.persons;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.annotations.Id;
import lombok.Data;


@Data
@DatabaseTable
public class Person {

    @Id
    @DatabaseField
    public int id;

    @DatabaseField
    public String name;


}
