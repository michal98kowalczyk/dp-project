package dp.orm.objects;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.annotations.Id;
import dp.orm.mapping.InheritanceMappingType;
import lombok.Data;


@Data
@DatabaseTable(inheritanceType = InheritanceMappingType.SINGLE_TABLE)
public class Users {

    @Id
    @DatabaseField
    private int id;

    @DatabaseField
    public String username;
}
