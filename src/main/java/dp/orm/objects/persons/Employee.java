package dp.orm.objects.persons;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.mapping.InheritanceMappingType;
import lombok.Data;

@Data
@DatabaseTable(inheritanceType = InheritanceMappingType.SINGLE_TABLE)
public class Employee extends Person {


    @DatabaseField
    public String company;
}
