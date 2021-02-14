package dp.orm.objects.Company;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import dp.orm.annotations.Id;
import dp.orm.annotations.JoinColumn;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@DatabaseTable
public class Address {

    @Id
    @DatabaseField
    private int id;


    @DatabaseField
    String street;


    @DatabaseField
    @JoinColumn(name = "customer_id", referencedClass = "Customer", referencedField = "id")
    public int customer_id;

}
