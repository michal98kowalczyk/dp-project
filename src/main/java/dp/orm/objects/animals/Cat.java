package dp.orm.objects.animals;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.annotations.Id;
import dp.orm.mapping.InheritanceMappingType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString(callSuper = true) @DatabaseTable(inheritanceType = InheritanceMappingType.CLASS_TABLE)
public class Cat  extends Animal{

    @Id
    @DatabaseField
    public  int catId;

    @DatabaseField
    public String owner;

    @DatabaseField
    public String name;


}
