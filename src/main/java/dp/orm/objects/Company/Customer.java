package dp.orm.objects.Company;

import dp.orm.annotations.*;
import dp.orm.mapping.InheritanceMappingType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
@Getter
@Setter
@ToString(callSuper = true) @DatabaseTable(inheritanceType = InheritanceMappingType.CLASS_TABLE)
public class Customer {
    @Id
    @DatabaseField
    public int id;

    @DatabaseField
    public String name;

    @DatabaseField
    @OneToMany
    public List<Contact> contacts;


}
