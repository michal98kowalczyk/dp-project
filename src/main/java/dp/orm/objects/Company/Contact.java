package dp.orm.objects.Company;

import com.google.j2objc.annotations.J2ObjCIncompatible;
import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.annotations.Id;
import dp.orm.annotations.JoinColumn;
import dp.orm.mapping.InheritanceMappingType;
import lombok.ToString;

@ToString(callSuper = true)
@DatabaseTable(inheritanceType = InheritanceMappingType.CLASS_TABLE)
public class Contact {
    @DatabaseField
    @Id
    public int id;

    @DatabaseField
    public String contactName;

    @DatabaseField
    @JoinColumn(name = "customer_id", referencedClass = "Customer", referencedField = "id")
    public int customer_id;
}
