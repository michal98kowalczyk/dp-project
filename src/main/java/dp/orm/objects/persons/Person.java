package dp.orm.objects.persons;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.annotations.Id;
import dp.orm.mapping.InheritanceMappingType;
import lombok.Data;


@Data
@DatabaseTable(inheritanceType = InheritanceMappingType.SINGLE_TABLE)
public class Person {

    @Id
    @DatabaseField
    private int id;

    @DatabaseField
    public String name;


}
