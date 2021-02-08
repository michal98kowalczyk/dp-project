package dp.orm.objects;

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
@DatabaseTable(inheritanceType = InheritanceMappingType.CONCRETE_TABLE)
public class Animal {

    @Id
    @DatabaseField
    private int id;

    @DatabaseField
    public String type = "";




}
