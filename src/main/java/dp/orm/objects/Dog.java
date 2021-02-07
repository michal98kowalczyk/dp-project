package dp.orm.objects;

import dp.orm.annotations.DatabaseField;
import dp.orm.annotations.DatabaseTable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
@DatabaseTable
public class Dog extends Animal{

    @DatabaseField
    private String owner;




}
