package dp.orm.objects.Company;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.annotations.Id;
import dp.orm.annotations.ForeignKey;
import dp.orm.mapping.InheritanceMappingType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@DatabaseTable(inheritanceType = InheritanceMappingType.CLASS_TABLE)
public class Contact {
    @DatabaseField
    @Id
    public int id;

    @DatabaseField
    public String contactName;

    @DatabaseField
    @ForeignKey(name = "customer_id", referencedClass = "Customer", referencedField = "id")
    public int customer_id;
}
