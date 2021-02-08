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
@ToString
@DatabaseTable(inheritanceType = InheritanceMappingType.CLASS_TABLE)
public class Animal {

    @Id
    @DatabaseField
    public int id;

    @DatabaseField
    public String type ;




}
